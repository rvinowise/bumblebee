package org.rvinowise.bumblebee_jumper.background;


import java.util.Collection;

import game.engine.Viewport;
import game.engine.units.animation.Animated;
import game.engine.units.animation.Animation;


public class Water extends Backgrownd {



    public void init(Viewport in_viewport, Animation in_animation,
                            Collection<Animated> in_engine_animated) {
        animation = in_animation;
        super.init(in_viewport, in_animation, in_engine_animated);
    }

    /*public static void create_first_instances(float in_y) {
        Point position = new Point(viewport.getRect().getLeft(), in_y);
        Point offset = new Point(getAnimation().getEssential_texture_scale().getX(), 0);
        int qty_needed = (int)Math.ceil(viewport.getRect().getWidth() / getAnimation().getEssential_texture_scale().getX());
        for (int i_instance = 0; i_instance < qty_needed; i_instance++) {
            Water new_backgrownd = new Water();
            new_backgrownd.setPosition(position);
            register_instance(new_backgrownd);
            position = position.plus(offset);
        }

    }*/



    public float getLevel() {
        return getLast_instance().getPosition().getY();
    }




}
