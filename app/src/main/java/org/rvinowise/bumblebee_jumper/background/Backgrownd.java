package org.rvinowise.bumblebee_jumper.background;


import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;

import game.engine.Viewport;
import game.engine.units.animation.Animated;
import game.engine.units.animation.Animation;
import game.engine.utils.primitives.Point;


public class Backgrownd {


    protected Deque<Animated> instances = new ArrayDeque<Animated>();
    protected Viewport viewport;

    protected Animation animation;
    protected Collection<Animated> engine_animated;


    public Animation getAnimation() {
        return animation;
    }
    public void init(Viewport in_viewport, Animation in_animation,
                            Collection<Animated> in_engine_animated) {
        viewport = in_viewport;
        animation = in_animation;
        engine_animated = in_engine_animated;
        //create_first_instances();
    }

    public  void create_first_instances(float in_y) {
        Point position = new Point(viewport.getRect().getLeft(), in_y);
        Point offset = new Point(getAnimation().getEssential_texture_scale().getX(), 0);
        int qty_needed = (int)Math.ceil(viewport.getRect().getWidth() / getAnimation().getEssential_texture_scale().getX());
        for (int i_instance = 0; i_instance < qty_needed; i_instance++) {
            Animated new_animated = new Animated();
            new_animated.setPosition(position);
            register_instance(new_animated);
            position = position.plus(offset);
        }

    }


    public void step_instances() {
        for (Animated instance: instances) {
            instance.step();
        }
    }

    public float getStandardWidth() {
        return animation.getEssential_texture_scale().getX();
    }

    public Collection<Animated> getInstances() {
        return instances;
    }

    public Animated getLast_instance() {
        return instances.peekLast();
    }


    public boolean no_more_instances_ahead() {
        Animated last_instance = getLast_instance();
        if (last_instance == null) {
            return true;
        }
        if (
                (last_instance.getPosition().getX() + getStandardWidth()/2) <
                viewport.getRect().getRight()
            ) {
            return true;
        }
        return false;
    }

    public void prolongate() {
        Point position = new Point(
                getLast_instance().getPosition().getX()+getStandardWidth(),
                getLast_instance().getPosition().getY()
        );
        Animated new_animated = new Animated();
        new_animated.setPosition(position);
        register_instance(new_animated);
    }

    public void register_instance(Animated new_instance) {
        instances.addLast(new_instance);
        new_instance.startAnimation(animation);
    }


}
