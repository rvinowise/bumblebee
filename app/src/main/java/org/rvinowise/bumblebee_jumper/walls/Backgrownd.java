package org.rvinowise.bumblebee_jumper.walls;


import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;

import game.engine.Viewport;
import game.engine.opengl.matrices.Matrix;
import game.engine.units.Drawable;
import game.engine.units.animation.Animated;
import game.engine.units.animation.Animation_type;
import game.engine.utils.primitives.Point;
import game.engine.utils.primitives.Rectangle;


public class Backgrownd extends Animated {


    protected static Deque<Animated> instances = new ArrayDeque<Animated>();
    protected static Viewport viewport;
    protected static Animation_type animation;
    protected static Collection<Animated> engine_animated;

    public static void init(Viewport in_viewport, Animation_type in_animation,
                            Collection<Animated> in_engine_animated) {
        viewport = in_viewport;
        animation = in_animation;
        engine_animated = in_engine_animated;
        create_first_instances();
    }

    private static void create_first_instances() {
        Point position = new Point(
                0,
                animation.getEssential_texture_scale().getX()/2
        );
        Animated new_instance = new Animated();
        new_instance.setPosition(position);
        new_instance.startAnimation(animation);
        instances.addLast(new_instance);
        engine_animated.add(new_instance);
    }

    public static void step_instances() {
        for (Animated instance: instances) {
            instance.step();
        }
    }

    public static float getStandardWidth() {
        return animation.getEssential_texture_scale().getX();
    }

    public static Collection<Animated> getInstances() {
        return instances;
    }

    public static Animated getLast_instance() {
        return instances.peekLast();
    }

    public Backgrownd() {
        this.startAnimation(animation);
    }


    public static boolean no_more_instances_ahead() {
        Animated last_instance = getLast_instance();
        if (
                (last_instance.getPosition().getX() + getStandardWidth()/2) <
                viewport.getRect().getRight()
            ) {
            return true;
        }
        return false;
    }

    public static void prolongate() {
        Point position = new Point(
                getLast_instance().getPosition().getX()+getStandardWidth(),
                getLast_instance().getPosition().getY()
        );
        Animated new_instance = new Animated();
        new_instance.setPosition(position);
        new_instance.startAnimation(animation);
        register_instance(new_instance);
    }

    public static void register_instance(Animated new_instance) {
        instances.addLast(new_instance);
        engine_animated.add(new_instance);
    }


}
