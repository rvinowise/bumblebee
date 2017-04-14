package game.engine.units;


import game.engine.opengl.matrices.Matrix;

public class Drawable extends Physical {
    private float direction;
    private float radius;

    public float getDirection() {
        return direction;
    }

    public void setDirection(float direction) {
        this.direction = direction;
    }

    public Matrix get_model_matrix() {
        Matrix model_matrix = new Matrix();
        model_matrix.clear();
        model_matrix.translate(this.getPosition());
        model_matrix.rotate(this.getDirection());
        return model_matrix;
    }

    @Override
    public float getRadius() {
        return radius;
    }

    public void setRadius(float in_radius) {
        radius = in_radius;
    }
}
