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

    public boolean has_inside(Point position) {
        if (
                (position.getX() < getLeft()) ||
                (position.getX() > getRight()) ||
                (position.getY() < getBottom()) ||
                (position.getY() > getTop())
            )
        {
            return false;
        }
        return true;
    }


    public Point get_nearest_point(Point position) {
        Point res = new Point();
        if (position.getX() < getLeft()) {
            res.setX(getLeft());
        } else if (position.getX() > getRight()) {
            res.setX(getRight());
        } else {
            res.setX(position.getX());
        }
        if (position.getY() < getBottom()) {
            res.setY(getBottom());
        } else if (position.getY() > getTop()) {
            res.setY(getTop());
        }else {
            res.setY(position.getY());
        }
        return res;
    }

    public void transpose(Point point) {
        top_left = top_left.plus(point);
        bottom_right = bottom_right.plus(point);
    }

    public void setCenterPosition(Point new_center) {
        Point old_center = top_left.plus(bottom_right).divide(2);
        Point top_left_offset = top_left.minus(old_center);
        Point bottom_right_offset = bottom_right.minus(old_center);
        top_left = new_center.plus(top_left_offset);
        bottom_right = new_center.plus(bottom_right_offset);
    }
}
