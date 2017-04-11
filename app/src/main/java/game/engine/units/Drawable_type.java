package game.engine.units;


import java.util.Vector;

import game.engine.opengl.Program;

public class Drawable_type {
    int texture;
    Program shader_program;
    Vector<Drawable> instances;

    protected Drawable_type() {

    }


    public int getTexture() {
        return texture;
    }

    public Vector<Drawable> getInstaces() {
        return instances;
    }
}

