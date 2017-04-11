package game.engine.units;

import game.engine.utils.primitives.Point;


public class Physical {


    protected Point position = new Point(0,0);

    protected Point vector = new Point(0,0);

    public Physical() {

    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public Point getVector() {
        return vector;
    }

    public void setVector(Point vector) {
        this.vector = vector;
    }

    public void step() {
    }
}
