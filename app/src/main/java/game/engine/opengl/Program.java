package game.engine.opengl;

import java.io.IOException;
import java.io.InputStream;

import android.opengl.GLES20;
import android.content.Context;
import android.util.Log;
import java.util.HashMap;
import java.nio.IntBuffer;
import static android.opengl.GLES20.*;


public class Program {

    private final static String TAG = "OpenGL";

    public static Program current = null;

    //public static HashMap SHADERS = new HashMap();
    private int program = -1;

    private HashMap uniforms = new HashMap();
    public HashMap attributes = new HashMap();



    private String vertexShaderName;
    private int vertexShaderHandle;
    private String fragmentShaderName;
    private int fragmentShaderHandle;

    private int last_attributeHandle = -1;


    public Program() {
        program = glCreateProgram();
    }

    public void add_shader(Context context, String file_name) {
        String[] groups = file_name.split("\\.");
        String file_type = groups[groups.length - 1];

        try {
            if (file_type.equals("vert")) {
                vertexShaderName = file_name;
                vertexShaderHandle = load_shader(context, file_name, GL_VERTEX_SHADER);
                glAttachShader(program, vertexShaderHandle);
            } else if (file_type.equals("frag")) {
                fragmentShaderName = file_name;
                fragmentShaderHandle = load_shader(context, file_name, GL_FRAGMENT_SHADER);
                glAttachShader(program, fragmentShaderHandle);
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    private int load_shader(Context context, String file, int shader_type) {
        String vertexShader;
        try {
            InputStream is = context.getAssets().open("shaders/"+file);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            vertexShader = new String(buffer);
        } catch(IOException e) {
            throw new RuntimeException("can't load shader", e);
        }

        int vertexShaderHandle = glCreateShader(shader_type);

        if (vertexShaderHandle != 0)
        {
            glShaderSource(vertexShaderHandle, vertexShader);
            glCompileShader(vertexShaderHandle);
            final int[] compileStatus = new int[1];
            glGetShaderiv(vertexShaderHandle, GL_COMPILE_STATUS, compileStatus, 0);
            if (compileStatus[0] == 0)
            {
                String error = glGetShaderInfoLog(vertexShaderHandle);
                Log.e("OpenGL","error:\n" + error);
                Log.e("OpenGL","shader: " + vertexShader);
                glDeleteShader(vertexShaderHandle);
            }
        } else {
            throw new RuntimeException("Error creating shader "+file);
        }
        return vertexShaderHandle;
    }


    public void link() {

        System.out.println(getVersion());

        glLinkProgram(program);

        final int[] linkStatus = new int[1];
        glGetProgramiv(program, GL_LINK_STATUS, linkStatus, 0);
        if (linkStatus[0] == 0)
        {
            String error = glGetProgramInfoLog(program);
            Log.e("OpenGL","error:\n" + error);

            glDeleteProgram(program);
            program = 0;
            throw new RuntimeException("can't link GL program: "+error);
        }
    }


    public void bind() {
        glUseProgram(program);
        Program.current = this;
    }


    public void unbind() {
        glUseProgram(0);
        Program.current = null;
    }

    /**
     * Define the uniform variable name to be used in the shader.
     * @param name
     */
    public void define_uniform(String name) {
        uniforms.put(name, glGetUniformLocation(program, name));
    }



    /**
     * Create an attribute index to be associated with a in variable name of a shader program.
     * The attribute index is used to bind attribute data within a Buffer Object.
     */
    public void bind_attribute(String attributeName) {
        last_attributeHandle++;
        attributes.put(attributeName,last_attributeHandle);
        glBindAttribLocation(program, last_attributeHandle, attributeName);
    }

    /**
     * Return the index associated with an attribute name
     * @param attributeName
     * @return
     */

    public int get_attribute(String attributeName)
    {
        return (Integer) attributes.get(attributeName);
    }

    public boolean contains_attribute(String attributeName)
    {
        return attributes.containsKey(attributeName);
    }

    /**
     *  Detach the shader from the program and delete it.
     */
    public void delete() {
        IntBuffer shaderCount = IntBuffer.allocate(16);

        glGetProgramiv(program, GL_ATTACHED_SHADERS, shaderCount);

        System.out.println("delete " + shaderCount.get(0) + " shaders");

        IntBuffer shaders = IntBuffer.allocate(shaderCount.get(0));

        glGetAttachedShaders(program, shaderCount.get(0), shaderCount, shaders);

        for (int i = 0; i < shaderCount.get(0); i++) {
            glDetachShader(program, shaders.get(i));
            glDeleteShader(shaders.get(i));
        }

        glUseProgram(0);
        glDeleteProgram(program);

    }


    public int getProgram() {
        return program;
    }


    public static String getVersion() {
        return "shader language version : " + glGetString(GL_SHADING_LANGUAGE_VERSION);
    }


    public boolean validate() {
        glValidateProgram(program);
        final int[] validateStatus = new int[1];
        glGetProgramiv(program, GL_VALIDATE_STATUS, validateStatus, 0);
        Log.v(TAG, "Results of validating program: " + validateStatus[0]
                + "\nLog:" + glGetProgramInfoLog(program));
        return validateStatus[0] != 0;
    }

}