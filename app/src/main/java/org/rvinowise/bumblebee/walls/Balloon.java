package org.rvinowise.bumblebee.walls;

import game.engine.units.animation.Animated;



public class Balloon extends Animated {

    static public float getStandardRadius() {
        return 0.4f;
    }

    public Balloon() {
        setRadius(getStandardRadius());
    }



}

