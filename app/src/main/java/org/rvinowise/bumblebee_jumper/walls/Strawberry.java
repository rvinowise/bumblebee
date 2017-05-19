package org.rvinowise.bumblebee_jumper.walls;

import org.rvinowise.bumblebee_jumper.R;

import java.util.Random;

import game.engine.Engine;
import game.engine.opengl.matrices.Matrix;
import game.engine.pos_functions.pos_functions;
import game.engine.units.animation.Animated;
import game.engine.units.animation.Animation;
import game.engine.units.animation.Effect;
import game.engine.utils.primitives.Point;


public class Strawberry extends Animated {

    static public float getStandardRadius() {
        return 0.5f;
    }
    static public float get_min_radius() {
        return 0.2f;
    }
    static public float get_max_radius() {
        return 0.8f;
    }
    static public float get_random_radius(Random random) {
        float radius = get_min_radius()+random.nextFloat()*(get_max_radius()-get_min_radius());
        return radius;
    }

    public Strawberry() {

        setRadius(getStandardRadius());
        startAnimation(Animation.valueOf(R.drawable.strawberry_full));
    }

    public Matrix get_model_matrix() {
        Matrix model_matrix = super.get_model_matrix();
        model_matrix.scale(new Point(1,1,1));
        return model_matrix;
    }

    public void explode(Animated exploder) {
        this.remove();

        Effect explode = Effect.create(Animation.valueOf(R.drawable.strawberry_explode), new Point(this.getPosition()),
                pos_functions.poidir(exploder.getPosition(), getPosition())-90);
        explode.setRadius(getRadius());
        leave_some_parts();
    }

    private void leave_some_parts() {
        Animated stalk = new Animated();
        stalk.startAnimation(Animation.valueOf(R.drawable.strawberry_stalk));
        stalk.setRadius(getRadius());
        stalk.setPosition(position);

    }

}

