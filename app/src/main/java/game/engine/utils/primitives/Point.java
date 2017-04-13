package game.engine.utils.primitives;



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

    public Point add(Point in_point) {
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
}

