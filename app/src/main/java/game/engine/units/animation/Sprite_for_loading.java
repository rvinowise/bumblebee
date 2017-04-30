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
    private Point essential_texture_scale;

    public Point getOffset_from_center() {
        return offset_from_center;
    }
    private Point offset_from_center;

    /*public static Sprite_for_loading prepare_for_loading(int in_res_id, Rectangle_shape in_rectangle) {
        Sprite_for_loading result = new Sprite_for_loading();

    }
    public static Sprite_for_loading prepare_for_loading(
            int in_res_id, int in_horizontal_qty, int in_vertical_qty, int size) {

    }*/


    public Sprite_for_loading(int in_res_id, Rectangle in_rectangle, int in_frames_qty) {
        this(in_res_id, in_rectangle, in_frames_qty, 1);
    }

    public Sprite_for_loading(int in_res_id, Rectangle in_rectangle, int in_frames_qty,
                              float scale) {
        this(in_res_id, in_rectangle, in_frames_qty, scale,
                new Point(in_rectangle.getWidth()/2, in_rectangle.getHeight()/2));
    }
    public Sprite_for_loading(int in_res_id, Rectangle in_rectangle, int in_frames_qty,
                              float scale, Point top_left_offset) {
        resource_id = in_res_id;
        sprite_rect = in_rectangle;
        frames_qty = in_frames_qty;
        this.offset_from_center = new Point(in_rectangle.getWidth()/2 - top_left_offset.getX(),
                top_left_offset.getY() - in_rectangle.getHeight()/2);

        float side_ratio = in_rectangle.getRatio();
        if (in_rectangle.getWidth() > in_rectangle.getHeight()) {
            essential_texture_scale = new Point(side_ratio, 1).multiply(scale);
        } else {
            essential_texture_scale = new Point(1, side_ratio).multiply(scale);
        }
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