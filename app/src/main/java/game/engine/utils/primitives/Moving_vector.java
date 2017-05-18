package game.engine.utils.primitives;

import game.engine.Fps_counter;


public class Moving_vector extends Point {

    public Moving_vector(float in_x, float in_y) {
        super(in_x, in_y);
    }
    public Moving_vector(float in_x, float in_y, float in_z) {
        super(in_x, in_y, in_z);
    }
    public Moving_vector(Point point) {
        super(point.x,point.y);
    }

    public Moving_vector getStep_value() {
        Moving_vector result = this.multiply(Fps_counter.getStep_multiplier());
        return result;
    }

    public Moving_vector plus(Point in_point) {
        Moving_vector result = new Moving_vector(
                in_point.x + this.x,
                in_point.y + this.y,
                in_point.z + this.z
        );
        return result;
    }
    public Moving_vector multiply(float arg) {
        Moving_vector result = new Moving_vector(
                this.x * arg,
                this.y * arg,
                0
        );
        return result;
    }

    public void brake_x_speed(float acceleration_left, float acceleration_right) {
        if (x > 0) {
            if (x > acceleration_left) {
                x = x + acceleration_left;
            } else {
                x = 0;
            }
        } else if (x < 0) {
            if (x < acceleration_right) {
                x = x + acceleration_right;
            } else {
                x = 0;
            }
        }
    }


    public void accelerate_y_speed(float acceleration, float max_speed) {
        if (acceleration < 0) {
            if (Math.abs(y) < Math.abs(max_speed - acceleration)) {
                y += acceleration;
            } else {
                y = max_speed;
            }
        }
    }
}
