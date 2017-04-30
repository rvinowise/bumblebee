package org.rvinowise.bumblebee_jumper.walls;

import org.rvinowise.bumblebee_jumper.R;

import game.engine.Engine;
import game.engine.opengl.matrices.Matrix;
import game.engine.units.animation.Animated;
import game.engine.units.animation.Animation;
import game.engine.utils.primitives.Point;


public class Strawberry extends Animated {

     static public float getStandardRadius() {
        return 0.4f;
    }

    public Strawberry() {

        setRadius(getStandardRadius());
        startAnimation(Animation.valueOf(R.drawable.strawberry));
    }

    public Matrix get_model_matrix() {
        Matrix model_matrix = super.get_model_matrix();
        model_matrix.scale(new Point(1,1,1));
        return model_matrix;
    }

    public void explode(Animated exploder) {
        Engine.getInstance().add_animated();
    }

}

