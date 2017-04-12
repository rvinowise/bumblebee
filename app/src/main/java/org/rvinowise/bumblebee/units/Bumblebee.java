package org.rvinowise.bumblebee.units;


import org.rvinowise.bumblebee.R;

import java.util.Vector;

import game.engine.units.animation.Animated;


public class Bumblebee extends Animated {


    public Bumblebee() {
        //startAnimation();
        //vector.setX(0.02f);
    }

    public static Vector<Integer> getAnimationNames() {
        Vector<Integer> result = new Vector<Integer>();
        result.add(R.drawable.anim_bumblebee_fly);

        return result;
    }

    @Override
    public void step() {
        super.step();
        position = position.add(vector);
    }

}
