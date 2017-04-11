package game.engine.opengl.primitives;


public class Shape {
    public static final int POSITION_OFFSET = 0;
    public static final int POSITION_DATA_SIZE = 3;
    public static final int TEXTURE_COORD_OFFSET = POSITION_DATA_SIZE;
    public static final int TEXRURE_COORD_DATA_SIZE = 2;

    public static final int BYTES_PER_FLOAT = 4;
    public static final int STRIDE_BYTES = (POSITION_DATA_SIZE+ TEXRURE_COORD_DATA_SIZE) * BYTES_PER_FLOAT;

    private Shape() {

    }
}
