package game.engine.units.animation;


import game.engine.Engine;
import game.engine.opengl.matrices.Matrix;
import game.engine.units.Physical;
import game.engine.utils.primitives.Point;

public class Animated extends Physical {
    private float current_frame;
    private Animation current_animation;
    //private float animation_speed = 1;
    private int steps_for_next_frame = 1;
    private int idle_steps = 0;

    private Point physical_size;
    private Point drowing_size;


    private Matrix texture_matrix = new Matrix();


    public Animated() {
        Engine.getInstance().add_animated(this);
        physical_size = new Point(1,1);
        //set_first_frame();
    }

    public void step() {
        super.step();
        process_animation();
    }

    private void process_animation() {
        idle_steps ++;
        if(idle_steps == steps_for_next_frame) {
            idle_steps = 0;
            set_next_frame();
        }
    }

    private void set_next_frame() {
        current_frame ++;
        if (current_frame >= current_animation.getFrames_qty()) {
            set_first_frame();
        } else {
            current_animation.setMatrix_to_next_frame(texture_matrix, (int)Math.floor(current_frame));
        }
    }

    private void set_first_frame() {
        current_frame=0;
        current_animation.setMatrix_to_first_frame(texture_matrix);
    }
    public boolean next_step_should_be_first() {
        return (
                (current_frame == current_animation.getFrames_qty()-1) &&
                (idle_steps == steps_for_next_frame-1)
        );
    }

    public void startAnimation(Animation in_animation) {
        if (current_animation != null) {
            if (!current_animation.equals(in_animation)) {
                current_animation.removeInstance(this);

            }
        }
        current_animation = in_animation;
        current_animation.addInstance(this);

        set_first_frame();
        update_size();
    }


    public Matrix getTexture_matrix() {
        return texture_matrix;
    }

    public Animation getCurrent_animation() {
        return current_animation;
    }

    public Matrix get_model_matrix() {
        Matrix model_matrix = new Matrix();
        model_matrix.clear();

        model_matrix.translate(this.getPosition());

        model_matrix.rotate(this.getDirection());
        model_matrix.scale(
                this.getDrowing_size()
        );
        model_matrix.translate(getCurrent_animation().getCenter_offset());

        return model_matrix;
    }

    public void setAnimation_speed(float animation_speed) {
        this.steps_for_next_frame = Math.round(1/animation_speed);
    }

    public void setSize(Point in_size) {
        physical_size = in_size;
        update_size();
    }
    private void update_size() {
        drowing_size = physical_size.multiply(getCurrent_animation().getEssential_texture_scale());
    }

    private Point getDrowing_size() {
        return drowing_size;
    }
    public Point getSize() {
        return physical_size;
    }
}
