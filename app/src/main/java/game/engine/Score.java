package game.engine;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.opengl.GLUtils;

import game.engine.opengl.Program;
import game.engine.opengl.Texture;
import game.engine.opengl.matrices.Matrix;

import static android.opengl.GLES20.*;

public class Score {

    final Texture texture = new Texture();
    Matrix matrix = new Matrix();

    public Score() {
        matrix.clear();
    }

    public void init_opengl() {
        glGenTextures(1, texture.getHandleRef(), 0);
        glBindTexture(GL_TEXTURE_2D, texture.getHandle());

        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
    }

    public void prepare_text(String text) {
        texture.bind();
        final Bitmap bitmap = Bitmap.createBitmap(256, 256, Bitmap.Config.ARGB_4444);
        final Canvas canvas = new Canvas(bitmap);
        Paint textPaint = new Paint();
        textPaint.setTextSize(60);
        textPaint.setAntiAlias(true);
        textPaint.setARGB(0xff, 0x00, 0x00, 0x00);

        canvas.drawText(text, 16,112, textPaint);
        GLUtils.texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);

        bitmap.recycle();
    }

    public void draw(Program shader_program) {
        texture.bind();
        glUniformMatrix4fv(shader_program.get_uniform("u_matrix"), 1, false, matrix.data(), 0);
        glUniformMatrix4fv(shader_program.get_uniform("u_texture_matrix"), 1, false, Matrix.getClear().data(), 0);
        glDrawArrays(GL_TRIANGLE_FAN, 0, 4);
    }
}
