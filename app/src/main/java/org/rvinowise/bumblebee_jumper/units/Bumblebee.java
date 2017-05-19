package org.rvinowise.bumblebee_jumper.units;


import android.util.Log;

import org.rvinowise.bumblebee_jumper.R;
import java.util.Vector;
import game.engine.units.animation.Animated;

import game.engine.utils.primitives.Point;


public class Bumblebee extends Animated {

    private float swoop_acceleration = 0.008f;
    private float swoop_max_speed = 0.25f;
    private boolean swooping = false;

    private float forward_acceleration = 0.01f;
    private float backward_acceleration = -0.0002f;

    private Point optimal_vector = new Point(0.1f,0.5f);


    private boolean live = true;

    public Bumblebee() {
        setRadius(0.5f);
        //startAnimation();
        //moving_vector.setX(0.02f);
    }

    public static Vector<Integer> getAnimationNames() {
        Vector<Integer> result = new Vector<Integer>();
        result.add(R.drawable.anim_bumblebee_fly);

        return result;
    }

    @Override
    public void step() {
        super.step();
        if (live) {
            if (isSwooping()) {
                swoop(90);
            } else {
                cruise_fly();
            }
        }
    }

    private void cruise_fly() {
        Point vector = getMoving_vector();


        if (getMoving_vector().getX() < optimal_vector.getX()) {
            final float needed_acceleration = optimal_vector.getX()- getMoving_vector().getX();
            if (needed_acceleration > forward_acceleration) {
                getMoving_vector().plus(forward_acceleration,0);
            } else {
                getMoving_vector().plus(needed_acceleration,0);
            }
        }

    }



    public void swoop(float direction) {
        moving_vector.accelerate_y_speed(-swoop_acceleration, -swoop_max_speed);
        moving_vector.brake_x_speed(backward_acceleration, forward_acceleration);
        Log.d("BUMBLE", String.valueOf(moving_vector.getX()));
    }

    public void start_swooping() {
        swooping = true;
    }
    public void stop_swooping() {
        swooping = false;
    }
    public boolean isSwooping() {
        return swooping;
    }


    /* Animal object */
    public void die() {
        live = false;
    }
    public boolean isLive() {
        return live;
    }


}
