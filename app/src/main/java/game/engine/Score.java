package game.engine;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.opengl.GLUtils;
import android.util.Log;

import game.engine.opengl.Program;
import game.engine.opengl.Texture;
import game.engine.opengl.matrices.Matrix;
import game.engine.units.animation.Animated;
import game.engine.units.animation.Animation;
import game.engine.utils.primitives.Point;
import static android.opengl.GLES20.*;

import com.google.android.gms.games.leaderboard.*;

import java.util.Collection;
import java.util.Vector;

public class Score {

    final Texture texture = new Texture();
    Matrix matrix = new Matrix();

    Bitmap bitmap;
    Paint textPaint;
    Canvas canvas;
    int value = 0;
    final int normal_color = Color.argb(0x77, 0x45, 0x45, 0x55);


    public Score() {
        matrix.clear();
        matrix.scale(new Point(2f,2f,1));

        bitmap = Bitmap.createBitmap(128, 128, Bitmap.Config.ARGB_4444);
        canvas = new Canvas(bitmap);
        textPaint = new Paint();

        textPaint.setTextSize(128);
        //textPaint.setTextSize(20);
        textPaint.setAntiAlias(true);

        textPaint.setColor(normal_color);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextScaleX(0.6f);
    }

    public void init_opengl() {
        glGenTextures(1, texture.getHandleRef(), 0);
        glBindTexture(GL_TEXTURE_2D, texture.getHandle());

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

    }

    public void prepare_text(String text) {
        texture.bind();

        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        canvas.drawText(text, bitmap.getWidth()/2,bitmap.getHeight()-20, textPaint); //bitmap.getWidth()/2
        GLUtils.texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);

    }



    public void draw(Program shader_program) {
        texture.bind();

        glUniformMatrix4fv(shader_program.get_uniform("u_matrix"), 1, false, matrix.data(), 0);
        glUniformMatrix4fv(shader_program.get_uniform("u_texture_matrix"), 1, false, Matrix.getClear().data(), 0);
        glUniform2fv(shader_program.get_uniform("u_texture_scale"), 1, new float[]{1, 1,}, 0);
        glDrawArrays(GL_TRIANGLE_FAN, 0, 4);
    }

    public int get_current() {
        return value;
    }

    public void add(int value) {
        this.value += value;
        prepare_text(String.valueOf(this.value));
        //prepare_text(String.valueOf(Fps_counter.getCurrent_fps()));
        Log.d("FPS",String.valueOf(Fps_counter.getCurrent_fps()));
    }

    public void setColor(int color) {
        textPaint.setColor(color);
        this.prepare_text(String.valueOf(get_current()));
    }

}
