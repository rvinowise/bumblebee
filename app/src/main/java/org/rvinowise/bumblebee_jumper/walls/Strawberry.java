package org.rvinowise.bumblebee_jumper.walls;

import org.rvinowise.bumblebee_jumper.R;

import game.engine.Engine;
import game.engine.opengl.matrices.Matrix;
import game.engine.pos_functions.pos_functions;
import game.engine.units.animation.Animated;
import game.engine.units.animation.Animation;
import game.engine.units.animation.Effect;
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
        this.remove();

        Effect.create(Animation.valueOf(R.drawable.strawberry_explode), this.getPosition(),
                pos_functions.poidir(exploder.getPosition(), getPosition())-90);
        //);)
        leave_some_parts();
    }

    private void leave_some_parts() {
        Animated collar = new Animated();
        collar.startAnimation(Animation.valueOf(R.drawable.strawberry_collar));
        collar.setPosition(position);
    }

}

