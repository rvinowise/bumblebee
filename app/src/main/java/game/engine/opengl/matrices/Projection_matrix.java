package game.engine.opengl.matrices;


import game.engine.utils.primitives.Rectangle;

public class Projection_matrix extends Matrix {

    public Projection_matrix(Rectangle view_rect, float near, float far) {
        super();
        ortho(view_rect, near, far);
    }

    public void frustum(Rectangle view_rect, float near, float far) {
        android.opengl.Matrix.frustumM(data(), 0,
                view_rect.getLeft(), view_rect.getRight(),
                view_rect.getBottom(), view_rect.getTop(), near, far);
    }
    public void ortho(Rectangle view_rect, float near, float far) {
        //android.opengl.Matrix.orthoM(data(), 0,
        //        view_rect.getLeft(), view_rect.getRight(),
        //        view_rect.getBottom(), view_rect.getTop(), near, far);
        android.opengl.Matrix.orthoM(data(), 0,
                view_rect.getLeft(), view_rect.getRight(),
                view_rect.getBottom(), view_rect.getTop(), near, far);
    }
    /*public void ortho(float float near, float far) {
        android.opengl.Matrix.orthoM(data(), 0,
                view_rect.getLeft(), view_rect.getRight(),
                view_rect.getBottom(), view_rect.getTop(), near, far);
    }*/

}
