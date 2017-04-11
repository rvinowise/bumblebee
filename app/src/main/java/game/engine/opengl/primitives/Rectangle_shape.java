package game.engine.opengl.primitives;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static game.engine.opengl.primitives.Shape.*;

public class Rectangle_shape {

    private static final float scale=1f;
    private static final float[] shape = {
            // X, Y, Z,
            // X Y
            -scale, -scale,
            0.0f, 1.0f,

            scale, -scale,
            1.0f, 1.0f,

            scale, scale,
            1.0f, 0.0f,

            -scale, scale,
            0.0f, 0.0f
    };



    private static FloatBuffer vertexBuffer;


    private static float[] getShape() {
        return shape;
    }

    public static void init() {
        vertexBuffer= ByteBuffer.allocateDirect(
                shape.length * BYTES_PER_FLOAT).
                order(ByteOrder.nativeOrder()).asFloatBuffer();
        vertexBuffer.put(shape);
    }

    public static FloatBuffer getVertexBuffer() {
        return vertexBuffer;
    }


}
