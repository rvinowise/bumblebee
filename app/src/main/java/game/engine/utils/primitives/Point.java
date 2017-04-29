package game.engine.utils.primitives;


import static java.lang.Math.*;

public class Point {

    private float x;
    private float y;



    private float z;

    public Point() {

    }
    public Point(float in_x, float in_y) {
        x = in_x;
        y = in_y;
        z = 0;
    }
    public Point(float in_x, float in_y, float in_z) {
        x = in_x;
        y = in_y;
        z = in_z;
    }

    public Point plus(Point in_point) {
        Point res_point = new Point(
                in_point.x + this.x,
                in_point.y + this.y,
                in_point.z + this.z
                );
        return res_point;
    }
    public Point minus(Point in_point) {
        Point res_point = new Point(
                this.x - in_point.x,
                this.y - in_point.y,
                this.z - in_point.z
        );
        return res_point;
    }
    public Point abs_minus(Point in_point) {
        Point res_point = new Point(
                signum(this.x) * abs(this.x) - in_point.x,
                signum(this.y) * abs(this.y) - in_point.y,
                signum(this.z) * abs(this.z) - in_point.z
        );
        return res_point;
    }

    public Point reversed() {
        Point res_point = new Point(
                -this.x,
                -this.y,
                -this.z
        );
        return res_point;
    }

    public float getX() {
        return x;
    }
    public void setX(float x) {
        this.x = x;
    }
    public float getY() {
        return y;
    }
    public void setY(float y) {
        this.y = y;
    }
    public float getZ() {
        return z;
    }
    public void setZ(float z) {
        this.z = z;
    }

    public boolean is_null() {
        return (x==0)&&(y==0);
    }

    public Point divide(float i) {
        Point res_point = new Point(
                this.x/2,
                this.y/2,
                this.z/2
        );
        return res_point;
    }

    public Point multiple(Point other) {
        Point res_point = new Point(
                this.x * other.x,
                this.y * other.y,
                0
        );
        return res_point;
    }

}

