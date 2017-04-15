package game.engine;


import static android.opengl.GLES20.*;

import java.util.Collection;

import game.engine.units.Physical;
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

    private Physical watched;
    //private Point watched_pos;
    //private Point watched_pos_luft = new Point(0, 1);
    private Rectangle watched_rect;

    public Viewport() {

    }

    public void watch_object(Physical in_watched, Rectangle in_rect) {
        watched = in_watched;
        watched_rect = in_rect;
    }

    public void setWatched_rect(Rectangle in_rect) {
         watched_rect = in_rect;
    }
    public void setWatched_rect(Point in_position) {
        watched_rect = new Rectangle(in_position.getX(),in_position.getX(),in_position.getY(),in_position.getY());
    }

    public void adjust_to_watched(Collection<Physical> physicals) {
        if (watched == null) {
            return;
        }
        //Point offset_from_ideal = watched.getPosition().minus(watched_rect.get_center());

        if (!watched_rect.has_inside(watched.getPosition())) {
            Point needed_offset =
                    watched_rect.get_nearest_point(watched.getPosition()).minus(
                            watched.getPosition());

            for (Physical physical : physicals) {
                physical.transpose(needed_offset);
            }
        }
    }

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
                eye_position.plus(new Point(0,0,2)),
                eye_position.plus(new Point(0,0,-5)),
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
    public Rectangle getRect() {
        return view_rect;
    }


    public void apply_view_to_matrix(Matrix model_matrix) {
        //Matrix res_matrix = new Matrix();
        //res_matrix.clear();
        model_matrix.multiply(view_matrix);
        model_matrix.multiply(projection_matrix);
        //return res_matrix;
    }

    public Rectangle getWatched_rect() {
        return watched_rect;
    }
}
