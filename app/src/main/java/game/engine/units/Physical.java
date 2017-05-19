package game.engine.units;

import game.engine.pos_functions.pos_functions;
import game.engine.utils.primitives.Moving_vector;
import game.engine.utils.primitives.Point;


abstract public class Physical {


    protected Point position = new Point(0,0);
    protected float direction;

    protected float radius=0.5f;
    protected Moving_vector moving_vector = new Moving_vector(0,0);
    
    boolean marked_for_remove = false;


    public Physical() {

    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public float getDirection() {
        return direction;
    }
    public void setDirection(float direction) {
        this.direction = direction;
    }
    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getRadius() {
        return radius;
    }
    public float getDiameter() {
        return radius*2;
    }

    public void transpose(Point in_position) {
        this.position.plus(in_position);
    }

    public Point getMoving_vector() {
        return moving_vector;
    }

    public void setMoving_vector(Point point) {
        this.moving_vector = new Moving_vector(point);
    }
    public void setVector(Moving_vector moving_vector) {
        this.moving_vector = moving_vector;
    }


    public void step() {
        position.plus(moving_vector.getStep_value());
    }

    public float getVectorDirection() {
        return pos_functions.poidir(new Point(0,0), getMoving_vector());
    }

    public float getVectorLength() {
        return pos_functions.poidis(new Point(0,0), getMoving_vector());
    }

    public void remove() {
        this.marked_for_remove = true;
    }
    public boolean isMarked_for_remove() {
        return marked_for_remove;
    }
}
