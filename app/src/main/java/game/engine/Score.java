package game.engine;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.opengl.GLUtils;

import game.engine.opengl.Program;
import game.engine.opengl.Texture;
import game.engine.opengl.matrices.Matrix;
import game.engine.utils.primitives.Point;

import static android.opengl.GLES20.*;

public class Score {

    final Texture texture = new Texture();
    Matrix matrix = new Matrix();

    Bitmap bitmap;
    Paint textPaint;
    Canvas canvas;

    public Score() {
        matrix.clear();
        matrix.scale(new Point(2,1,1));

        bitmap = Bitmap.createBitmap(256, 256, Bitmap.Config.ARGB_4444);
        canvas = new Canvas(bitmap);
        textPaint = new Paint();

        textPaint.setTextSize(20);
        textPaint.setAntiAlias(true);
        //textPaint.setARGB(0xff, 0x00, 0x00, 0x00);
    }

    public void init_opengl() {
        glGenTextures(1, texture.getHandleRef(), 0);
        glBindTexture(GL_TEXTURE_2D, texture.getHandle());

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
    }

    public void prepare_text(String text) {
        texture.bind();

        //textPaint.setARGB(0x00, 0xff, 0xff, 0xff);
        //canvas.drawRect(0,0,256,256,textPaint);
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        textPaint.setARGB(0xff, 0x00, 0x00, 0x00);
        canvas.drawText(text, 10,20, textPaint);
        GLUtils.texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);

        //bitmap.recycle();
    }

    public void draw(Program shader_program) {
        texture.bind();

        glUniformMatrix4fv(shader_program.get_uniform("u_matrix"), 1, false, matrix.data(), 0);
        glUniformMatrix4fv(shader_program.get_uniform("u_texture_matrix"), 1, false, Matrix.getClear().data(), 0);
        glDrawArrays(GL_TRIANGLE_FAN, 0, 4);
    }
}
