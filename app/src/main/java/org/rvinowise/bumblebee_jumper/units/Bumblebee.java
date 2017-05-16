package org.rvinowise.bumblebee_jumper.units;


import android.util.Log;

import org.rvinowise.bumblebee_jumper.R;
import java.util.Vector;
import game.engine.units.animation.Animated;

import game.engine.pos_functions.*;
import game.engine.utils.primitives.Point;


public class Bumblebee extends Animated {

    private float swoop_acceleration = 0.008f;
    private float swoop_max_speed = 0.25f;
    private boolean swooping = false;

    private float forward_acceleration = 0.01f;
    private float backward_acceleration = -0.0010f;//-forward_acceleration;

    private Point optimal_vector = new Point(0.1f,0.5f);


    private boolean live = true;

    public Bumblebee() {
        setRadius(0.5f);
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
        if (live) {
            if (isSwooping()) {
                swoop(90);
            } else {
                cruise_fly();
            }
        }
    }

    private void cruise_fly() {
        Point vector = getVector();

        /*correct_fast_backward_flying(vector);
        correct_fast_forward_flying(vector);
        correct_fast_upward_flying(vector);*/

        if (getVector().getX() < optimal_vector.getX()) {
            final float needed_acceleration = optimal_vector.getX()-getVector().getX();
            if (needed_acceleration > forward_acceleration) {
                setVector(getVector().plus(new Point(forward_acceleration,0)));
            } else {
                setVector(getVector().plus(new Point(needed_acceleration,0)));
            }
        }

    }

    private void correct_fast_backward_flying(Point vector) {
        final float braking_backward = 0.02f;
        if (vector.getX() < 0) {
            vector.setX(vector.getX() + braking_backward);
        }
    }
    private void correct_fast_forward_flying(Point vector) {
        final float braking_forward = 0.01f;
        if (vector.getX() > optimal_vector.getX()) {
            vector.setX(vector.getX()- braking_forward);
        }
    }
    private void correct_fast_upward_flying(Point vector) {
        final float braking_upward = 0.01f;
        if (vector.getY() > optimal_vector.getY()) {
            vector.setY(vector.getY() - braking_upward);
        }
    }


    public void swoop(float direction) {
        //vector = vector.plus(pos_functions.lendir(swoop_acceleration, direction));
        vector.accelerate_y_speed(-swoop_acceleration, -swoop_max_speed);
        vector.brake_x_speed(backward_acceleration, forward_acceleration);
        Log.d("BUMBLE", String.valueOf(vector.getX()));
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
