package game.engine.opengl.matrices;

//import android.opengl.Matrix;

import game.engine.utils.primitives.Point;


public class Matrix {
    protected float[] m_data = new float[16];

    public Matrix() {

    }

    public float[] data() {
        return m_data;
    }

    public Matrix clear() {
        android.opengl.Matrix.setIdentityM(m_data, 0);
        return this;
    }

    public Matrix translate(Point in_point) {
        android.opengl.Matrix.translateM(m_data, 0, in_point.getX(), in_point.getY(), 0);
        return this;
    }
    public Matrix rotate(float direction) {
        android.opengl.Matrix.rotateM(m_data, 0, direction, 0, 0, 1);
        return this;
    }
    public void multiply(Matrix in_matrix1, Matrix in_matrix2) {
        android.opengl.Matrix.multiplyMM(m_data, 0, in_matrix1.data(), 0, in_matrix2.data(), 0);
    }
    public void multiply(Matrix in_matrix) {
        android.opengl.Matrix.multiplyMM(m_data, 0, in_matrix.data(), 0, m_data, 0);
    }
}
