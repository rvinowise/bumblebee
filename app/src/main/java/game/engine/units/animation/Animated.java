package game.engine.units.animation;


import java.util.Vector;

import game.engine.opengl.matrices.Matrix;
import game.engine.units.Drawable;
import game.engine.utils.primitives.Point;

public class Animated extends Drawable {
    private int current_frame;



    private Animation_type current_animation;

    private Matrix texture_matrix = new Matrix();


    public Animated() {
        //set_first_frame();
    }

    public void step() {
        super.step();
        set_next_frame();
    }

    private void set_next_frame() {
        current_frame++;
        if (current_frame >= current_animation.getFrames_qty()) {
            set_first_frame();
        } else {
            current_animation.setMatrix_to_next_frame(texture_matrix, current_frame);
        }
    }

    private void set_first_frame() {
        current_frame=0;
        current_animation.setMatrix_to_first_frame(texture_matrix);
        //current_animation.setMatrix_to_frame(texture_matrix, 0);

    }

    public void startAnimation(Animation_type in_animation_type) {
        if (current_animation != null) {
            if (!current_animation.equals(in_animation_type)) {
                current_animation.removeInstance(this);

            }
        }
        current_animation = in_animation_type;
        current_animation.addInstance(this);

        set_first_frame();
    }


    public Matrix getTexture_matrix() {
        return texture_matrix;
    }

    public Animation_type getCurrent_animation() {
        return current_animation;
    }
}
