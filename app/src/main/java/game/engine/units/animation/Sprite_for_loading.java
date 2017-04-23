package game.engine.units.animation;

import game.engine.utils.primitives.Point;
import game.engine.utils.primitives.Rectangle;

public class Sprite_for_loading {
    private int resource_id;
    private Rectangle sprite_rect;
    private int horizontal_qty;
    private int vertical_qty;
    private int frames_qty;

    public Point getEssential_texture_scale() {
        return essential_texture_scale;
    }

    public void setEssential_texture_scale(Point essential_texture_scale) {
        this.essential_texture_scale = essential_texture_scale;
    }

    private Point essential_texture_scale;

    /*public static Sprite_for_loading prepare_for_loading(int in_res_id, Rectangle_shape in_rectangle) {
        Sprite_for_loading result = new Sprite_for_loading();

    }
    public static Sprite_for_loading prepare_for_loading(
            int in_res_id, int in_horizontal_qty, int in_vertical_qty, int size) {

    }*/


    public Sprite_for_loading(int in_res_id, Rectangle in_rectangle, int in_frames_qty) {
        this(in_res_id, in_rectangle, in_frames_qty, new Point(1,1));
    }

    public Sprite_for_loading(int in_res_id, Rectangle in_rectangle, int in_frames_qty,
                              float in_essential_texture_scale) {
        resource_id = in_res_id;
        sprite_rect = in_rectangle;
        frames_qty = in_frames_qty;
        essential_texture_scale = new Point(in_essential_texture_scale, in_essential_texture_scale);
    }
    public Sprite_for_loading(int in_res_id, Rectangle in_rectangle, int in_frames_qty,
                              Point in_essential_texture_scale) {
        resource_id = in_res_id;
        sprite_rect = in_rectangle;
        frames_qty = in_frames_qty;
        essential_texture_scale = in_essential_texture_scale;
    }

    /*public Sprite_for_loading(
            int in_res_id, int in_horizontal_qty, int in_images_qty, float is_essential_texture_scale) {
        resource_id = in_res_id;
        horizontal_qty = in_horizontal_qty;
        frames_qty = in_images_qty;
        essential_texture_scale = is_essential_texture_scale;
    }
    public Sprite_for_loading(
            int in_res_id, int in_horizontal_qty, int in_images_qty) {
        this(in_res_id, in_horizontal_qty, in_images_qty, 1);
    }*/


    public int getResource_id() {
        return resource_id;
    }

    public Rectangle getSprite_rect() {
        return sprite_rect;
    }

    public int getHorizontal_qty() {
        return horizontal_qty;
    }

    public int getVertical_qty() {
        return vertical_qty;
    }
    public int getFrames_qty() {
        return frames_qty;
    }


}