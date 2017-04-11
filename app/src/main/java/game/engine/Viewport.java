package game.engine;


import android.opengl.GLES20;
import static android.opengl.GLES20.*;
import android.os.Build;
import android.support.annotation.RequiresApi;

import game.engine.utils.primitives.Rectangle;
import game.engine.opengl.matrices.Matrix;
import game.engine.opengl.matrices.Projection_matrix;
import game.engine.opengl.matrices.View_matrix;
import game.engine.utils.primitives.Point;

public class Viewport {


    private View_matrix view_matrix;
    private Projection_matrix projection_matrix;
    private Rectangle view_rect;
    private float scale_of_short = 1;
    private Point eye_position;

    public Viewport() {

    }
    /*public Viewport(Rectangle in_view_rect) {
        set_view_dimension((int)in_view_rect.getWidth(), (int)in_view_rect.getHeight());
    }*/

    //@RequiresApi(api = Build.VERSION_CODES.FROYO)

    public void set_scale_of_shortest_side(float in_scale) {
        scale_of_short = in_scale;
    }


    public void set_view_dimension(int width, int height) {

        final float aspectRatio = width > height ?
                (float) width / (float) height :
                (float) height / (float) width;
        final float scale = scale_of_short/2;
        if (width > height) {
            view_rect = new Rectangle( -aspectRatio*scale, aspectRatio*scale,
                    -scale, scale);
        } else {
            view_rect = new Rectangle( -scale, scale,
                    -aspectRatio*scale, aspectRatio*scale);
        }

        glViewport(0,0,width, height);
        /*this.view_rect = new Rectangle(
                eye_position.getX()-width/2, eye_position.getX()+width/2,
                eye_position.getY()+height/2, eye_position.getY()-height/2
        );*/

        projection_matrix = new Projection_matrix(this.view_rect, -10, 10);
    }

    public void set_view_rect(Rectangle in_view_rect) {
        this.eye_position = in_view_rect.get_center();
        this.view_rect = in_view_rect;
        update_matrices_according_to_my_state();

    }
    private void update_matrices_according_to_my_state() {
        view_matrix = new View_matrix(
                eye_position.add(new Point(0,0,2)),
                eye_position.add(new Point(0,0,-5)),
                new Point(0,1,0)
        );
        projection_matrix = new Projection_matrix(this.view_rect, 1, 10);
    }

    public View_matrix getView_matrix() {
        return view_matrix;
    }
    public Projection_matrix getProjection_matrix() {
        return projection_matrix;
    }
    public Rectangle getView_rect() {
        return view_rect;
    }


    public void apply_view_to_matrix(Matrix model_matrix) {
        //Matrix res_matrix = new Matrix();
        //res_matrix.clear();
        model_matrix.multiply(view_matrix);
        model_matrix.multiply(projection_matrix);
        //return res_matrix;
    }
}
