package game.engine.opengl.matrices;

import game.engine.utils.primitives.Point;


public class View_matrix extends Matrix {

    private final Point eye_position;// = new Point(0,0,2);
    private final Point look_vector;// = new Point(0,0,-5);
    private final Point up_vector;// = new Point(0,1,0);

    public View_matrix(Point in_eye_position,
                       Point in_look_vector,
                       Point in_up_vector) {
        super();
        this.eye_position = in_eye_position;
        this.look_vector = in_look_vector;
        this.up_vector = in_up_vector;
        set_matrix_according_to_data();
    }

    private void set_matrix_according_to_data() {
        android.opengl.Matrix.setLookAtM(data(), 0,
                eye_position.getX(), eye_position.getY(), eye_position.getZ(),
                look_vector.getX(), look_vector.getY(), look_vector.getZ(),
                up_vector.getX(), up_vector.getY(), up_vector.getZ());
    }
}
