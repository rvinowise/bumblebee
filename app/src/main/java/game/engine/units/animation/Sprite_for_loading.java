package game.engine.units.animation;

import game.engine.utils.primitives.Rectangle;

public class Sprite_for_loading {
    private int resource_id;
    private Rectangle sprite_rect;
    private int horizontal_qty;
    private int vertical_qty;
    private int size;

    /*public static Sprite_for_loading prepare_for_loading(int in_res_id, Rectangle_shape in_rectangle) {
        Sprite_for_loading result = new Sprite_for_loading();

    }
    public static Sprite_for_loading prepare_for_loading(
            int in_res_id, int in_horizontal_qty, int in_vertical_qty, int size) {

    }*/


    public Sprite_for_loading(int in_res_id, Rectangle in_rectangle) {
        resource_id = in_res_id;
        sprite_rect = in_rectangle;
    }

    public Sprite_for_loading(
            int in_res_id, int in_horizontal_qty, int in_vertical_qty, int in_size) {
        resource_id = in_res_id;
        horizontal_qty = in_horizontal_qty;
        vertical_qty = in_vertical_qty;
        size = in_size;
    }


    public void requestSprite(int in_res_id, Rectangle in_rectangle) {
        resource_id = in_res_id;
        sprite_rect = in_rectangle;
    }
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


}