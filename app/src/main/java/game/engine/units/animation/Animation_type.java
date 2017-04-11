package game.engine.units.animation;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import game.engine.opengl.Texture;
import game.engine.opengl.matrices.Matrix;
import game.engine.utils.primitives.Point;
import game.engine.utils.primitives.Rectangle;

public class Animation_type {
    private Texture texture = new Texture();
    private int frames_qty = 0;
    private Set<Animated> instances = new HashSet<Animated>();

    private Point frame_offset;


    public Animation_type() {}


    public Texture getTexture() {
        return texture;
    }
    public void prepare_to_draw_instances() {
        texture.bind();
    }
    /*public void draw_instances(Viewport viewport) {
        Vector<Animated> animated_instances = getInstaces();
        for (Animated animated_instance: animated_instances) {
            Matrix model_matrix = animated_instance.get_model_matrix();
            viewport.apply_view_to_matrix(model_matrix);

            drawSprite(texture);
        }
    }*/

    public int getFrames_qty() {
        return frames_qty;
    }

    public void addInstance(Animated in_animated) {
        instances.add(in_animated);
    }
    public Collection<Animated> getInstances() {
        return instances;
    }
    public void removeInstance(Animated in_animated) {
        instances.remove(in_animated);
    }


    public Point getFrame_offset() {
        return frame_offset;
    }

}

