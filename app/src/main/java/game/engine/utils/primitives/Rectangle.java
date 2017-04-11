package game.engine.utils.primitives;


public class Rectangle {

    Point top_left;
    Point bottom_right;

    public Rectangle(float left, float right, float bottom, float top) {
        top_left = new Point(left, top);
        bottom_right = new Point(right, bottom);
    }
    public Rectangle(int width, int height) {
        top_left = new Point(-width/2, height/2);
        bottom_right = new Point(width/2, -height/2);
    }

    public Point get_center() {
        Point center = new Point(
                (this.getLeft()+(this.getRight() - this.getLeft())/2),
                (this.getBottom()+(this.getTop() - this.getBottom())/2)
        );
        return center;
    }

    public float getLeft() {
        return top_left.getX();
    }
    public float getRight() {
        return bottom_right.getX();
    }
    public float getTop() {
        return top_left.getY();
    }
    public float getBottom() {
        return bottom_right.getY();
    }
    public float getWidth() {
        return bottom_right.getX() - top_left.getX();
    }
    public float getHeight() {
        return top_left.getY() - bottom_right.getY();
    }
}
