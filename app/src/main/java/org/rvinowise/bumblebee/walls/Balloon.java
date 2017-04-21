package org.rvinowise.bumblebee.walls;

import org.rvinowise.bumblebee.R;

import game.engine.opengl.matrices.Matrix;
import game.engine.units.animation.Animated;
import game.engine.units.animation.Animation_type;
import game.engine.utils.primitives.Point;


public class Balloon extends Animated {

     static public float getStandardRadius() {
        return 0.4f;
    }

    public Balloon() {

        setRadius(getStandardRadius());
        startAnimation(Animation_type.get_animation(R.drawable.strawberry));
    }

    public Matrix get_model_matrix() {
        Matrix model_matrix = super.get_model_matrix();
        model_matrix.scale(new Point(1,1,1));
        return model_matrix;
    }

}

