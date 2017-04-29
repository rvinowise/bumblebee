package game.engine.units;

import game.engine.pos_functions.pos_functions;
import game.engine.utils.primitives.Point;


abstract public class Physical {


    protected Point position = new Point(0,0);
    protected float direction;



    protected float radius;

    protected Point vector = new Point(0,0);



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

    public void setVector(Point vector) {
        this.vector = vector;
    }

    public void step() {
        position = position.plus(vector);
    }

    public float getVectorDirection() {
        return pos_functions.poidir(new Point(0,0), getVector());
    }

    public float getVectorLength() {
        return pos_functions.poidis(new Point(0,0), getVector());
    }
}
