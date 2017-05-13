package game.engine.units;

import game.engine.pos_functions.pos_functions;
import game.engine.utils.primitives.Point;
import game.engine.utils.primitives.Vector;


abstract public class Physical {


    protected Point position = new Point(0,0);
    protected float direction;

    protected float radius;
    protected Vector vector = new Vector(0,0);
    
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

    public void transpose(Point in_position) {
        this.position = this.position.plus(in_position);
    }

    public Point getVector() {
        return vector;
    }

    public void setVector(Point point) {
        this.vector = new Vector(point);
    }
    public void setVector(Vector vector) {
        this.vector = vector;
    }

    /*public void step() {
        position = position.plus(vector);
    }*/
    public void step() {
        position = position.plus(vector.getStep_value());
        //position = position.plus(vector);
    }

    public float getVectorDirection() {
        return pos_functions.poidir(new Point(0,0), getVector());
    }

    public float getVectorLength() {
        return pos_functions.poidis(new Point(0,0), getVector());
    }

    public void remove() {
        this.marked_for_remove = true;
    }
    public boolean isMarked_for_remove() {
        return marked_for_remove;
    }
}
