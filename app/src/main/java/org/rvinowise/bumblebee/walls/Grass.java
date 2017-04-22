package org.rvinowise.bumblebee.walls;


import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;

import game.engine.Viewport;
import game.engine.opengl.matrices.Matrix;
import game.engine.units.animation.Animated;
import game.engine.units.animation.Animation_type;
import game.engine.utils.primitives.Point;
import game.engine.utils.primitives.Rectangle;


public class Grass extends Animated {


    private Point top_left = new Point();
    private Rectangle rect;
    private static Deque<Grass> instances = new ArrayDeque<Grass>();
    private static Viewport viewport;
    private static Animation_type animation;
    private static Collection<Animated> engine_animated;

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
                animation.getEssential_texture_scale()/2+2
        );
        Grass new_instance = new Grass();
        new_instance.setPosition(position);
        instances.addLast(new_instance);
        engine_animated.add(new_instance);
    }

    public static void step_instances() {
        for (Grass instance: instances) {
            instance.step();
        }
    }

    public static float getStandardWidth() {
        return animation.getEssential_texture_scale();
    }

    public static Collection<Grass> getInstances() {
        return instances;
    }

    public static Grass getLast_instance() {
        return instances.peekLast();
    }

    public Grass() {
        this.startAnimation(animation);
        rect = new Rectangle(-getStandardWidth()/2,getStandardWidth()/2,0f,1f);
    }

    public float getLevel() {
        return top_left.getY();
    }


    public static boolean no_more_instances_ahead() {
        Grass last_instance = getLast_instance();
        if (
                (last_instance.getPosition().getX() + getStandardWidth()*2) <
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
        Grass new_instance = new Grass();
        new_instance.setPosition(position);
        instances.addLast(new_instance);
        engine_animated.add(new_instance);
    }

    public Matrix get_model_matrix() {
        Matrix model_matrix = super.get_model_matrix();
        model_matrix.scale(new Point(getStandardWidth(), getStandardWidth()*4));
        //model_matrix.scale(new Point(getStandardWidth(),2));
        return model_matrix;
    }


    public Rectangle getRect() {
        return rect;
    }


}
