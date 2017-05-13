package game.engine.utils.primitives;

import game.engine.Fps_counter;


public class Vector extends Point {

    public Vector(float in_x, float in_y) {
        super(in_x, in_y);
    }
    public Vector(float in_x, float in_y, float in_z) {
        super(in_x, in_y, in_z);
    }
    public Vector(Point point) {
        super(point.x,point.y);
    }

    public Vector getStep_value() {
        Vector result = (Vector) this.multiply(Fps_counter.getStep_multiplier());
        return result;
    }

    public Vector plus(Point in_point) {
        Vector result = new Vector(
                in_point.x + this.x,
                in_point.y + this.y,
                in_point.z + this.z
        );
        return result;
    }
    public Vector multiply(float arg) {
        Vector result = new Vector(
                this.x * arg,
                this.y * arg,
                0
        );
        return result;
    }

}
