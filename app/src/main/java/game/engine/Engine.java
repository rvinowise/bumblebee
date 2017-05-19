package game.engine;


import android.content.Context;
//import android.opengl.GLES32;
import static android.opengl.GLES20.*;

import android.graphics.Color;
import android.opengl.GLSurfaceView;
//import android.opengl.Matrix;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Collection;
import java.util.Vector;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import game.engine.initialisation.Sprite_loader;
import game.engine.opengl.matrices.Matrix;
import game.engine.opengl.primitives.Rectangle_shape;
import game.engine.pos_functions.pos_functions;
import game.engine.units.animation.Animated;
import game.engine.units.animation.Animation;
import game.engine.units.Physical;
import game.engine.opengl.Program;

public abstract class Engine
        implements GLSurfaceView.Renderer

{
    private Context context;



    private game.engine.opengl.Program shader_program;

    final private Vector<Animated> animateds = new Vector<Animated>();
    final private Vector<Animation> backgrownd_animations = new Vector<Animation>();
    final private Vector<Animation> foregrownd_animations = new Vector<Animation>();

    final private Viewport viewport = new Viewport();
    final private Human_control control = new Human_control();

    protected Score score;

    static Engine instance;
    protected Handler handler_menu;

    private long last_cleaning_outside;
    private float time_before_cleaning_outside = 1f;
    private boolean epilog;

    public boolean isEpilog() {
        return epilog;
    }
    public void start_epilog() {
        epilog = true;
        final int epilog_score_color = Color.argb(0xDD, 0xFF, 0x33, 0x00);
        Color.rgb(0xFF, 0x99, 0x55);
        score.setColor(epilog_score_color);
    }

    public interface System_listener {
        void return_score_to_start_screen();
    }
    protected System_listener system_listener;
    public void setSystem_listener(System_listener listener) {
        this.system_listener = listener;
    }

    protected Engine() {
        instance = this;
        score = new Score();
    }

    public static Engine getInstance() {
        return instance;
    }

    public void setContext(Context in_context) {
        context = in_context;
    }
    public Context getContext() {
        return context;
    }


    private void init_gl(Context context) {
        prepare_graphic_settings();

        load_sprites(context);
        load_shaders(context);
        init_score();
        init_primitives();
    }


    private void init_score() {

        score.init_opengl();
        score.prepare_text("0");
    }


    public void add_animated(Animated in_animated) {
        animateds.add(in_animated);
    }


    public void step() {
        for(int i_physical = 0; i_physical < animateds.size(); i_physical++) {
            Animated animated = animateds.get(i_physical);
            if (no_need_more(animated)) {
                remove(i_physical);
            } else {
                animated.step();
            }
        }
        viewport.adjust_to_watched(animateds);

        if (is_time_to_check_outside()) {
            remove_outside_animateds();
        }
    }

    private void remove_outside_animateds() {
        last_cleaning_outside = Fps_counter.getLast_physics_step_moment();
        for(int i_physical = 0; i_physical < animateds.size(); i_physical++) {
            Animated animated = animateds.get(i_physical);
            if (is_outside(animated)) {
                remove(i_physical);
            }
        }
    }

    private boolean is_time_to_check_outside() {
        return (Fps_counter.getLast_physics_step_moment() - last_cleaning_outside)/ 1000000000f
                > time_before_cleaning_outside;
    }

    protected boolean no_need_more(Animated animated) {
        if (animated.isMarked_for_remove()) {
            return true;
        }
        return false;
    }
    abstract protected boolean is_outside(Animated animated);

    protected void remove(int i_physical) {
        Physical physical = animateds.get(i_physical);
        if (physical != null) {
            Animated animated = (Animated)physical;
            animated.getCurrent_animation().removeInstance(animated);
        }

        animateds.remove(i_physical);
    }

    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config)
    {
        init_gl(context);
    }


    private void prepare_graphic_settings() {
        glClearColor(0.7f, 0.8f, 1f, 1f);
        glEnable(GL_CULL_FACE);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    private void load_shaders(Context context) {
        try {
            shader_program = new Program();
            shader_program.add_shader(context, "sprite.vert");
            shader_program.add_shader(context, "sprite.frag");

            shader_program.define_uniform("u_matrix");
            shader_program.define_uniform("u_texture_matrix");
            shader_program.define_uniform("u_texture_scale");
            shader_program.bind_attribute("a_position");
            shader_program.bind_attribute("a_texture_position");


            shader_program.link();
            shader_program.validate();
            shader_program.bind();
        } catch (RuntimeException e) {
            shader_program.delete();
            Log.e("OpenGL", "error: "+e.getMessage());
        }
    }

    private void init_primitives() {
        Rectangle_shape.init();
    }

    abstract public Sprite_loader getSprite_loader();

    protected void load_sprites(Context context) {
        Sprite_loader sprite_loader = getSprite_loader();
        sprite_loader.setContext(context);
        sprite_loader.load_sprites_to_animations(
                sprite_loader.getBackground_sprites(), backgrownd_animations);
        sprite_loader.load_sprites_to_animations(
                sprite_loader.getForeground_sprites(), foregrownd_animations);

    }


    public abstract void init_scene();




    public void register_first_step_as_done() {
        Fps_counter.register_first_step_as_done();
    }

    @Override
    public void onDrawFrame(GL10 glUnused)
    {

        Fps_counter.drawing_step();

        if (Fps_counter.its_time_for_next_step()) {
            Fps_counter.physics_step();
            step();
        }

        draw();
    }


    /*private void process_fps(float in_delay) {
        final float relevant_delay_difference = 0.000001f;
        if (abs(last_delay - in_delay) > relevant_delay_difference) {
            //value.prepare_text(String.valueOf(in_delay));
        }
        last_delay = in_delay;
    }*/

    private void draw() {
        glClear(GL_COLOR_BUFFER_BIT);
        prepare_to_draw_sprites();

        if (isEpilog()) {
            draw_epilog();
        } else {
            draw_game_objects();
        }
    }
    private void draw_game_objects() {
        draw_animated_units(backgrownd_animations);
        score.draw(shader_program);
        draw_animated_units(foregrownd_animations);
    }
    private void draw_epilog() {
        draw_animated_units(backgrownd_animations);
        draw_animated_units(foregrownd_animations);
        score.draw(shader_program);
    }

    private void draw_animated_units(Collection<Animation> animations) {
        for (Animation animation : animations) {
            animation.prepare_to_draw_instances(shader_program);

            for (Animated animated: animation.getInstances()) {
                prepare_to_draw_instance(animated);

                glDrawArrays(GL_TRIANGLE_FAN, 0, 4);
            }
        }
    }

    private void prepare_to_draw_instance(Animated in_animated) {
        Matrix final_matrix = in_animated.get_model_matrix();
        final_matrix.multiply(viewport.getProjection_matrix());

        glUniformMatrix4fv(shader_program.get_uniform("u_matrix"), 1, false,
                final_matrix.data(),
                0);
        glUniformMatrix4fv(shader_program.get_uniform("u_texture_matrix"), 1, false, in_animated.getTexture_matrix().data(), 0);
    }


    private void prepare_to_draw_sprites() {

        int bytes_per_float = 4;
        int mPositionDataSize = 2;
        int mTextureCoordDataSize = 2;

        int mTextureCoordOffset = 2*4;

        int mStrideBytes = (mPositionDataSize+mTextureCoordDataSize)*bytes_per_float;


        Rectangle_shape.getVertexBuffer().position(0);
        glVertexAttribPointer(
                shader_program.get_attribute("a_position"), mPositionDataSize, GL_FLOAT, false,
                mStrideBytes, Rectangle_shape.getVertexBuffer());
        glEnableVertexAttribArray(shader_program.get_attribute("a_position"));

        Rectangle_shape.getVertexBuffer().position(mPositionDataSize);
        glVertexAttribPointer(
                shader_program.get_attribute("a_texture_position"), mTextureCoordDataSize, GL_FLOAT, false,
                mStrideBytes, Rectangle_shape.getVertexBuffer());
        glEnableVertexAttribArray(shader_program.get_attribute("a_texture_position"));
    }






    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height)
    {
        viewport.set_view_resolution(width, height);

    }

    public void onResume() {

    }

    public void onPause() {

    }




    public Vector<Animated> getAnimateds() {
        return animateds;
    }

    public Vector<Animation> getBackgrownd_animations() {
        return backgrownd_animations;
    }
    public Viewport getViewport() {
        return viewport;
    }


    public boolean onTouch(View v, MotionEvent event) {
        return control.onTouch(v, event);
    }

    protected Human_control getControl() {
        return control;
    }

    public Collection<Animated> getCollided_circle(Animated in_physical) {
        Vector<Animated> result = new Vector<Animated>();
        for (Animated physical: animateds) {
            final float collision_distance = in_physical.getRadius() + physical.getRadius();
            final float real_distance = pos_functions.poidis(in_physical.getPosition(), physical.getPosition());
            if (real_distance <= collision_distance) {
                result.add(physical);
            }
        }
        return result;
    }
    public Collection<Animated> getCollided_rect(Animated in_physical) {
        Vector<Animated> result = new Vector<Animated>();
        for (Animated physical: animateds) {
            final float collision_distance = in_physical.getRadius() + physical.getRadius();
            final float diff_x = in_physical.getPosition().getX() - physical.getPosition().getX();
            final float diff_y = in_physical.getPosition().getY() - physical.getPosition().getY();
            if (
                    (Math.abs(diff_x) <= collision_distance)&&
                    (Math.abs(diff_y) <= collision_distance)
                    )
            {
                result.add(physical);
            }
        }
        return result;
    }

    public void setHandler_menu(Handler handler_menu) {
        this.handler_menu = handler_menu;
    }

    public Score getScore() {
        return score;
    }
}
