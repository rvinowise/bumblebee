package game.engine.units.animation;

import android.graphics.Bitmap;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import game.engine.opengl.Program;
import game.engine.opengl.Texture;
import game.engine.opengl.matrices.Matrix;
import game.engine.utils.primitives.Point;
import game.engine.utils.primitives.Rectangle;

import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform2fv;

public class Animation_type {
    private Texture texture = new Texture();
    private int frames_qty = 0;
    private Rectangle sprite_rect;

    private Point texture_scale;
    private int qty_in_row;

    private Point frame_offset;

    private Set<Animated> instances = new HashSet<Animated>();


    public Animation_type(Bitmap bmp, Rectangle in_rect, int frames_qty) {
        sprite_rect = in_rect;
        qty_in_row = (int) Math.ceil(bmp.getWidth()/sprite_rect.getWidth());
        this.frames_qty = frames_qty;
        final float white_space_width = (bmp.getWidth()-(sprite_rect.getWidth()*qty_in_row)) / bmp.getWidth();
        final int filled_rows_qty = (int) Math.ceil((float)frames_qty / qty_in_row);
        final float white_space_height = (bmp.getHeight() - (sprite_rect.getHeight()*filled_rows_qty)) / bmp.getHeight();
        texture_scale = new Point(
                (1-white_space_width)/ qty_in_row,
                (1-white_space_height) / filled_rows_qty
        );
    }

    public Texture getTexture() {
        return texture;
    }

    public void prepare_to_draw_instances(Program shader_program) {
        texture.bind();

        int u_texture_scale_location = glGetUniformLocation(shader_program.getProgram(), "u_texture_scale");
        glUniform2fv(u_texture_scale_location, 1,
                new float[]{
                        getTexture_scale().getX(),
                        getTexture_scale().getY(),
                },
                0);
    }

    public int getFrames_qty() {
        return frames_qty;
    }

    public void addInstance(Animated in_animated) {
        instances.add(in_animated);
    }
    public Collection<Animated> getInstances() {
        return instances;
    }
    public void removeInstance(Animated in_animated) {
        instances.remove(in_animated);
    }


    public Matrix setMatrix_to_first_frame(Matrix matrix) {
        matrix.clear();
        return matrix;
    }

    public Matrix setMatrix_to_next_frame(Matrix matrix, int frame) {
        if (is_first_frame_of_next_row(frame)) {
            matrix.clear();
            final int current_row = (int) Math.ceil(frame / qty_in_row);
            matrix.translate(new Point(0,texture_scale.getY()*current_row));
        } else {
            matrix.translate(new Point(texture_scale.getX(),0));
        }

        return matrix;
    }

    private boolean is_first_frame_of_next_row(int in_frame) {
        return (in_frame%qty_in_row == 0);
    }

    public Point getTexture_scale() {
        return texture_scale;
    }


}

