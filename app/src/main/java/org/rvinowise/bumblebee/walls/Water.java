package org.rvinowise.bumblebee.walls;


import org.rvinowise.bumblebee.BumblebeeEngine;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Vector;

import game.engine.Viewport;
import game.engine.opengl.Texture;
import game.engine.units.animation.Animated;
import game.engine.units.animation.Animation_type;
import game.engine.utils.primitives.Point;
import game.engine.utils.primitives.Rectangle;


public class Water extends Animated {


    Point top_left = new Point();
    //Rectangle rect;
    static Deque<Water> instances = new ArrayDeque<Water>();
    static Viewport viewport;
    static Animation_type animation;
    static Collection<Animated> engine_animated;

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
                -3
        );
        Water new_water = new Water();
        new_water.setPosition(position);
        instances.addLast(new_water);
    }

    public static float getStandardWidth() {
        return 4;
    }

    public static Collection<Water> getInstances() {
        return instances;
    }

    public static Water getLast_instance() {
        return instances.getLast();
    }

    public Water() {
        this.startAnimation(animation);
    }

    public float getLevel() {
        return top_left.getY();
    }


    public static boolean no_water_ahead() {
        Water last_instance = getLast_instance();
        if (
                (last_instance.getPosition().getX() + getStandardWidth()) <
                viewport.getRect().getRight()
            ) {
            return true;
        }
        return false;
    }

    public static void prolongate() {
        Point position = new Point(
                getLast_instance().getPosition().getX()+getStandardWidth()*2,
                getLast_instance().getPosition().getY()
        );
        Water new_water = new Water();
        new_water.setPosition(position);
        instances.offer(new_water);
    }


}
