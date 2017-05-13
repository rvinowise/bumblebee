package game.engine;


import android.content.Context;
//import android.opengl.GLES32;
import static android.opengl.GLES20.*;
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
import game.engine.units.animation.Effect;
import game.engine.opengl.Program;

import static java.lang.Math.*;

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

    private boolean initialized = false;

    protected Score score;

    static Engine instance;
    protected Handler handler_menu;

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
        score.prepare_text("9999");
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
        //value.prepare_text();

    }

    protected boolean no_need_more(Animated animated) {
        if (animated.isMarked_for_remove()) {
            return true;
        }
        return false;
    }

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
        //glClearDepthf(1.0f);
        glEnable(GL_CULL_FACE);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        //glEnable( GL_DEPTH_TEST );
        //glDepthFunc( GL_LEQUAL );
        //glDepthMask( true );

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





    private Fps_counter fps_counter;

    public void register_first_step_as_done() {
        fps_counter.register_first_step_as_done();
    }

    @Override
    public void onDrawFrame(GL10 glUnused)
    {

        fps_counter.drawing_step();

        //final float fastest_framerate = 0.033f;

        if (fps_counter.its_time_for_next_step()) {
            fps_counter.physics_step();
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

    public void draw() {
        glClear(GL_COLOR_BUFFER_BIT);

        prepare_to_draw_sprites();
        for (Animation animation : backgrownd_animations) {
            animation.prepare_to_draw_instances(shader_program);

            for (Animated animated: animation.getInstances()) {
                prepare_to_draw_instance(animated);

                glDrawArrays(GL_TRIANGLE_FAN, 0, 4);
            }
        }
        score.draw(shader_program);
        for (Animation animation : foregrownd_animations) {
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

    public void setHandler_menu(Handler handler_menu) {
        this.handler_menu = handler_menu;
    }

    public Score getScore() {
        return score;
    }
}
