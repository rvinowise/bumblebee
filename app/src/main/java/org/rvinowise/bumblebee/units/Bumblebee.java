package org.rvinowise.bumblebee.units;


import org.rvinowise.bumblebee.R;
import java.util.Vector;
import game.engine.units.animation.Animated;

import game.engine.pos_functions.*;


public class Bumblebee extends Animated {

    private float rush_speed = 0.01f;

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

    }

    public void rush(float direction) {
        vector = vector.plus(pos_functions.lendir(rush_speed, direction));
    }
}
