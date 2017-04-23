package org.rvinowise.bumblebee_jumper.walls;


import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;

import game.engine.Viewport;
import game.engine.opengl.Program;
import game.engine.opengl.matrices.Matrix;
import game.engine.units.animation.Animated;
import game.engine.units.animation.Animation_type;
import game.engine.utils.primitives.Point;
import game.engine.utils.primitives.Rectangle;


public class Water extends Backgrownd {

    public static void init(Viewport in_viewport, Animation_type in_animation,
                            Collection<Animated> in_engine_animated) {
        Backgrownd.init(in_viewport, in_animation, in_engine_animated);
    }

    private static void create_first_instances(float in_y) {
        Point position = new Point(0, in_y);
        int qty_needed = (int) (viewport.getRect().getWidth() / animation.getEssential_texture_scale().getX());
        for (int i_instance = 0; i_instance < qty_needed; i_instance++) {

        }
        Water new_water = new Water();
        new_water.setPosition(position);
        instances.addLast(new_water);
        engine_animated.add(new_water);
    }


    public float getLevel() {
        return getLast_instance().getPosition().getY();
    }




}
