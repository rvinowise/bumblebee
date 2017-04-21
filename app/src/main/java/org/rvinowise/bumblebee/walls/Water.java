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
import game.engine.opengl.matrices.Matrix;
import game.engine.units.animation.Animated;
import game.engine.units.animation.Animation_type;
import game.engine.utils.primitives.Point;
import game.engine.utils.primitives.Rectangle;


public class Water extends Animated {


    Point top_left = new Point();
    Rectangle rect;
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
                -4
        );
        Water new_water = new Water();
        new_water.setPosition(position);
        instances.addLast(new_water);
        engine_animated.add(new_water);
    }

    public static void step_instances() {
        for (Water water: instances) {
            water.step();
        }
    }

    public static float getStandardWidth() {
        return 8;
    }

    public static Collection<Water> getInstances() {
        return instances;
    }

    public static Water getLast_instance() {
        return instances.peekLast();
    }

    public Water() {
        this.startAnimation(animation);
        rect = new Rectangle(-getStandardWidth()/2,getStandardWidth()/2,0f,1f);
    }

    public float getLevel() {
        return top_left.getY();
    }


    public static boolean no_water_ahead() {
        Water last_instance = getLast_instance();
        if (
                (last_instance.getPosition().getX() + getStandardWidth()/4) <
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
        Water new_water = new Water();
        new_water.setPosition(position);
        instances.addLast(new_water);
        engine_animated.add(new_water);
    }

    public Matrix get_model_matrix() {
        Matrix model_matrix = super.get_model_matrix();
        model_matrix.scale(new Point(rect.getWidth(),rect.getHeight(),1));
        return model_matrix;
    }


    public Rectangle getRect() {
        return rect;
    }


}
