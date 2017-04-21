package game.engine.units.animation;

import android.graphics.Bitmap;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import game.engine.opengl.Program;
import game.engine.opengl.Texture;
import game.engine.opengl.matrices.Matrix;
import game.engine.utils.primitives.Point;
import game.engine.utils.primitives.Rectangle;

import static android.opengl.GLES20.glUniform2fv;

public class Animation_type {
    private Texture texture = new Texture();
    private int frames_qty = 0;
    private Rectangle sprite_rect;

    private Point animation_texture_scale;
    public float essential_texture_scale;
    private int qty_in_row;

    private Point frame_offset;

    private Set<Animated> instances = new HashSet<Animated>();
    private static Map<Integer, Animation_type> animation_types =
            new HashMap<Integer, Animation_type>();

    public static Animation_type get_animation(int in_id) {
        return animation_types.get(in_id);
    }

    public Animation_type(Bitmap bmp, Rectangle in_rect, int frames_qty) {
        sprite_rect = in_rect;
        qty_in_row = (int) Math.ceil(bmp.getWidth()/sprite_rect.getWidth());
        this.frames_qty = frames_qty;
        final float white_space_width = (bmp.getWidth()-(sprite_rect.getWidth()*qty_in_row)) / bmp.getWidth();
        final int filled_rows_qty = (int) Math.ceil((float)frames_qty / qty_in_row);
        final float white_space_height = (bmp.getHeight() - (sprite_rect.getHeight()*filled_rows_qty)) / bmp.getHeight();
        animation_texture_scale = new Point(
                (1-white_space_width)/ qty_in_row,
                (1-white_space_height) / filled_rows_qty
        );
    }

    public Animation_type(Bitmap bmp, Sprite_for_loading in_sprite_data) {
        sprite_rect = in_sprite_data.getSprite_rect();
        qty_in_row = (int) Math.ceil(bmp.getWidth()/sprite_rect.getWidth());
        this.frames_qty = in_sprite_data.getFrames_qty();
        final float white_space_width = (bmp.getWidth()-(sprite_rect.getWidth()*qty_in_row)) / bmp.getWidth();
        final int filled_rows_qty = (int) Math.ceil((float)frames_qty / qty_in_row);
        final float white_space_height = (bmp.getHeight() - (sprite_rect.getHeight()*filled_rows_qty)) / bmp.getHeight();
        animation_texture_scale = new Point(
                (1-white_space_width)/ qty_in_row,
                (1-white_space_height) / filled_rows_qty
        );
        essential_texture_scale = in_sprite_data.getEssential_texture_scale();
        animation_types.put(in_sprite_data.getResource_id(), this);
    }

    public Texture getTexture() {
        return texture;
    }

    public void prepare_to_draw_instances(Program shader_program) {
        texture.bind();

        glUniform2fv(shader_program.get_uniform("u_texture_scale"), 1,
                new float[]{
                        getAnimation_texture_scale().getX(),
                        getAnimation_texture_scale().getY(),
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
            matrix.translate(new Point(0, animation_texture_scale.getY()*current_row));
        } else {
            matrix.translate(new Point(animation_texture_scale.getX(),0));
        }

        return matrix;
    }

    private boolean is_first_frame_of_next_row(int in_frame) {
        return (in_frame%qty_in_row == 0);
    }

    public Point getAnimation_texture_scale() {
        return animation_texture_scale;
    }


}

