package game.engine;


import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
//import android.opengl.GLES32;
import static android.opengl.GLES20.*;
import android.opengl.GLSurfaceView;
//import android.opengl.Matrix;
import android.opengl.GLUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Collection;
import java.util.Vector;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import game.engine.opengl.matrices.Matrix;
import game.engine.opengl.primitives.Rectangle_shape;
import game.engine.pos_functions.pos_functions;
import game.engine.units.animation.Animated;
import game.engine.units.animation.Animation_type;
import game.engine.units.Physical;
import game.engine.units.animation.Sprite_for_loading;
import game.engine.opengl.Program;
import game.engine.utils.primitives.Point;

import static java.lang.Math.*;

public abstract class Engine
        implements GLSurfaceView.Renderer

{
    private Context context;



    private game.engine.opengl.Program shader_program;

    final private Vector<Physical> physicals = new Vector<Physical>();
    final private Vector<Animation_type> animation_types = new Vector<Animation_type>();

    final private Viewport viewport = new Viewport();
    final private Human_control control = new Human_control();

    private boolean initialized = false;

    final private Score score = new Score();


    protected Engine() {

    }

    public void setContext(Context in_context) {
        context = in_context;
    }

    public void init(Context context) {

        init_gl(context);
        init_logic();
    }

    private void init_gl(Context context) {
        prepare_graphic_settings();
        load_sprites(context);
        load_shaders(context);
        init_score();
    }

    private void init_logic() {
        init_primitives();
        init_scene();

    }

    private void init_score() {
        score.init_opengl();
        score.prepare_text("123");
    }


    private boolean opengl_supported(Context context) {
        final ActivityManager activityManager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;

        if (supportsEs2) return true;
        return false;
    }


    public void add_physical(Physical in_physical) {
        physicals.add(in_physical);
    }

    public void step() {
        for(int i_physical = 0; i_physical < physicals.size(); i_physical++) {
            Physical physical = physicals.get(i_physical);
            if (no_need_more(physical)) {
                remove(i_physical);
            } else {
                physical.step();
            }
        }
        viewport.adjust_to_watched(physicals);
        //score.prepare_text();

    }

    protected abstract boolean no_need_more(Physical physical);

    protected void remove(int i_physical) {
        Physical physical = physicals.get(i_physical);
        if (physical instanceof Animated) {
            Animated animated = (Animated)physical;
            animated.getCurrent_animation().removeInstance(animated);
        }

        physicals.remove(i_physical);
    }

    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config)
    {
        if (!initialized) {
            init(context);
            initialized = true;
        }


    }


    private void prepare_graphic_settings() {
        glClearColor(0.5f, 0.5f, 0.5f, 1f);
        //glClearDepthf(1.0f);
        glEnable(GL_CULL_FACE);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        //glEnable( GL_DEPTH_TEST );
        //glDepthFunc( GL_LEQUAL );
        //glDepthMask( true );

    }

    private void load_shaders(Context context) {
        try {
            shader_program = new Program();
            shader_program.add_shader(context, "sprite.vert");
            shader_program.add_shader(context, "sprite.frag");

            shader_program.define_uniform("u_matrix");
            shader_program.define_uniform("u_texture_matrix");
            shader_program.define_uniform("u_texture_scale");
            shader_program.bind_attribute("a_position");
            shader_program.bind_attribute("a_texture_position");


            shader_program.link();
            shader_program.validate();
            shader_program.bind();
        } catch (RuntimeException e) {
            shader_program.delete();
            Log.e("OpenGL", "error: "+e.getMessage());
        }
    }

    private void init_primitives() {
        Rectangle_shape.init();
    }

    abstract public Vector<Sprite_for_loading> getSprites_for_loading();

    protected void load_sprites(Context context) {
        Vector<Sprite_for_loading> sprites = getSprites_for_loading();
        for (Sprite_for_loading sprite: sprites) {
            Bitmap bmp = load_bitmap_for_sprite(context, sprite);

            Animation_type animation_type = new Animation_type(
                    bmp, sprite);
            glGenTextures(1, animation_type.getTexture().getHandleRef() , 0);
            if (animation_type.getTexture().getHandle() == 0) {
                throw new RuntimeException("can't create texture");
            }


            glBindTexture(GL_TEXTURE_2D, animation_type.getTexture().getHandle());
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            GLUtils.texImage2D(GL_TEXTURE_2D, 0, bmp, 0);
            bmp.recycle();

            animation_types.add(animation_type);
        }

    }

    private Bitmap load_bitmap_for_sprite(Context context, Sprite_for_loading sprite) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        Bitmap bmp = BitmapFactory.decodeResource(
                context.getResources(), sprite.getResource_id(), options);
        return bmp;
    }


    public void init_scene() {
        moment_of_last_step = System.nanoTime();
    }

    float moment_of_last_step;
    float last_delay = 0;

    @Override
    public void onDrawFrame(GL10 glUnused)
    {

        final float current_moment = System.nanoTime();
        final float time_since_last_step = (current_moment - moment_of_last_step) / 1000000000f;
        final float framerate = 0.02f;
        if (time_since_last_step > framerate) {
            step();
            moment_of_last_step = current_moment;
            process_fps(time_since_last_step);

        }
        draw();



    }

    private void process_fps(float in_delay) {
        final float relevant_delay_difference = 0.000001f;
        if (abs(last_delay - in_delay) > relevant_delay_difference) {
            score.prepare_text(String.valueOf(in_delay));
        }
        last_delay = in_delay;
    }

    public void draw() {
        glClear(GL_COLOR_BUFFER_BIT);

        prepare_to_draw_sprites();
        for (Animation_type animation_type: animation_types) {
            animation_type.prepare_to_draw_instances(shader_program);

            for (Animated animated: animation_type.getInstances()) {
                prepare_to_draw_instance(animated);

                glDrawArrays(GL_TRIANGLE_FAN, 0, 4);
            }
        }

        score.draw(shader_program);
    }

    private void prepare_to_draw_instance(Animated in_animated) {
        Matrix final_matrix = in_animated.get_model_matrix();
        final_matrix.multiply(viewport.getProjection_matrix());

        glUniformMatrix4fv(shader_program.get_uniform("u_matrix"), 1, false,
                final_matrix.scale(
                        in_animated.getCurrent_animation().getEssential_texture_scale()
                ).data(),
                0);
        glUniformMatrix4fv(shader_program.get_uniform("u_texture_matrix"), 1, false, in_animated.getTexture_matrix().data(), 0);
    }


    private void prepare_to_draw_sprites() {

        int bytes_per_float = 4;
        int mPositionDataSize = 2;
        int mTextureCoordDataSize = 2;

        int mTextureCoordOffset = 2*4;

        int mStrideBytes = (mPositionDataSize+mTextureCoordDataSize)*bytes_per_float;


        Rectangle_shape.getVertexBuffer().position(0);
        glVertexAttribPointer(
                shader_program.get_attribute("a_position"), mPositionDataSize, GL_FLOAT, false,
                mStrideBytes, Rectangle_shape.getVertexBuffer());
        glEnableVertexAttribArray(shader_program.get_attribute("a_position"));

        Rectangle_shape.getVertexBuffer().position(mPositionDataSize);
        glVertexAttribPointer(
                shader_program.get_attribute("a_texture_position"), mTextureCoordDataSize, GL_FLOAT, false,
                mStrideBytes, Rectangle_shape.getVertexBuffer());
        glEnableVertexAttribArray(shader_program.get_attribute("a_texture_position"));
    }






    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height)
    {
        viewport.set_view_resolution(width, height);
    }

    public void onResume() {

    }

    public void onPause() {

    }




    public Vector<Physical> getPhysicals() {
        return physicals;
    }

    public Vector<Animation_type> getAnimations() {
        return animation_types;
    }
    public Viewport getViewport() {
        return viewport;
    }



    /*public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this fragment
        setRetainInstance(true);
    }*/

    public boolean onTouch(View v, MotionEvent event) {
        return control.onTouch(v, event);
    }

    protected Human_control getControl() {
        return control;
    }

    public Collection<Physical> getCollided_circle(Physical in_physical) {
        Vector<Physical> result = new Vector<Physical>();
        for (Physical physical: physicals) {
            final float collision_distance = in_physical.getRadius() + physical.getRadius();
            final float real_distance = pos_functions.poidis(in_physical.getPosition(), physical.getPosition());
            if (real_distance <= collision_distance) {
                result.add(physical);
            }
        }
        return result;
    }
}
