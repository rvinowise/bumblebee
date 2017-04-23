package org.rvinowise.bumblebee_jumper;

import org.rvinowise.bumblebee_jumper.units.Bumblebee;
import org.rvinowise.bumblebee_jumper.walls.Balloon;
import org.rvinowise.bumblebee_jumper.walls.Grass;
import org.rvinowise.bumblebee_jumper.walls.Water;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;

import game.engine.Engine;
import game.engine.pos_functions.pos_functions;
import game.engine.units.Drawable;
import game.engine.units.Physical;
import game.engine.units.animation.Animation_type;
import game.engine.units.animation.Sprite_for_loading;
import game.engine.units.animation.Animated;
import game.engine.utils.primitives.Point;
import game.engine.utils.primitives.Rectangle;


public class BumblebeeEngine extends Engine
{

    private Set<Balloon> balloons = new HashSet<Balloon>();
    private Bumblebee bumblebee;

    private Units_generator generator = new Units_generator(this);

    public Collection<Balloon> getBalloons() {
        return balloons;
    }

    private float water_height = -2;


    public BumblebeeViewport getBumblebeeViewport() {
        return bumblebee_viewport;
    }

    BumblebeeViewport bumblebee_viewport;


    public BumblebeeEngine() {
        //viewport = new BumblebeeViewport(super.getViewport());
    }


    @Override
    public void init_scene() {


        super.getViewport().set_scale_of_shortest_side(7);

        bumblebee = new Bumblebee();
        this.add_physical(bumblebee);
        bumblebee.startAnimation(Animation_type.get_animation(R.drawable.anim_bumblebee_fly));
        bumblebee.setPosition(new Point(0, (float) 0.1));
        getPhysicals().lastElement().setVector(new Point(0.1f, -0.00f));
        //animated.setDirection(330);

        init_viewport();

        Animated balloon = add_balloon();
        balloon.setPosition(bumblebee.getPosition().plus(new Point(4, -2)));

        balloon = add_balloon();
        balloon.setPosition(bumblebee.getPosition().plus(new Point(7, -3)));

        Water.init(super.getViewport(), Animation_type.get_animation(R.drawable.water), (Collection)getPhysicals());
        Grass.init(super.getViewport(), Animation_type.get_animation(R.drawable.grass), (Collection)getPhysicals());



        super.init_scene();
    }

    private void init_viewport() {
        bumblebee_viewport = new BumblebeeViewport(super.getViewport(), bumblebee);
        super.getViewport().watch_object(bumblebee);
    }


    private boolean is_resolution_known() {
        return (getViewport().getRect() != null);
    }

    public Balloon add_balloon() {
        Balloon balloon = new Balloon();
        super.add_physical(balloon);
        balloons.add(balloon);
        return balloon;
    }
    protected void remove(int i_physical) {
        Physical physical = getPhysicals().get(i_physical);
        if (physical instanceof Balloon) {
            balloons.remove(physical);
        }

        super.remove(i_physical);
    }

    @Override
    public void step() {
        player_control();
        process_player_physics();
        process_maybe_changing_viewport();
        process_elementary_physics();
        Water.step_instances();


        generator.step();
    }

    public void process_maybe_changing_viewport() {

        if (Water.getLast_instance() == null) {
            getBumblebeeViewport().setWatch_upto_bottom(0);
        } else {
            final float water_y = Water.getLast_instance().getPosition().getY();
            float distance_to_water = bumblebee.getPosition().getY() - water_y;
            if (
                    (getBumblebeeViewport().getWatch_upto_bottom() == 0) &&
                            (distance_to_water < super.getViewport().getRect().getHeight() / 2)
                    ) {
                getBumblebeeViewport().setWatch_upto_bottom(super.getViewport().getRect().getBottom());
            } else if (
                    (getBumblebeeViewport().getWatch_upto_bottom() < 0) &&
                            (distance_to_water >= super.getViewport().getRect().getHeight() / 2)
                    ) {
                getBumblebeeViewport().setWatch_upto_bottom(0);
            }
        }

    }

    private void process_elementary_physics() {
        super.step();
    }

    private void process_player_physics() {
        final Point gravity_vector = new Point(0f,-0.001f);
        bumblebee.setVector(bumblebee.getVector().plus(gravity_vector));

        final Point go_forward_vector = new Point(0.01f,0f);
        if (bumblebee.getVector().getX() < 0.1) {
            bumblebee.setVector(bumblebee.getVector().plus(go_forward_vector));
        }

        Collection<Physical> collided = getCollided_circle(bumblebee);
        player_jump_from_balloons(collided);
    }

    private void player_jump_from_balloons(Collection<Physical> collided) {
        for (Physical physical: collided) {
            if (physical instanceof Balloon) {
                jump_from_balloon(bumblebee, (Balloon)physical);
            }
        }
    }

    private void jump_from_balloon(Physical jumper, Balloon from) {
        final Point prev_position = jumper.getPosition().minus(jumper.getVector());
        final float dir_to_balloon = pos_functions.poidir(prev_position, from.getPosition());
        float corner =pos_functions.corner(jumper.getVectorDirection(), dir_to_balloon);
        float bounce_dir = (dir_to_balloon-180) + corner;
        float bounce_speed = jumper.getVectorLength();
        jumper.setVector(pos_functions.lendir(bounce_speed, bounce_dir));
    }


    private void player_control() {
        if (getControl().isTouched()) {
            bumblebee.rush(90);
        }
    }

    @Override
    protected boolean no_need_more(Physical physical) {
        return is_left_map(physical);
    }

    private boolean is_left_map(Physical physical) {
        if (physical instanceof Water) {
            return is_backgroind_object_left_map((Animated) physical);
        }
        if (physical instanceof Grass) {
            return is_backgroind_object_left_map((Animated) physical);
        }

        if (physical.getPosition().getX() < -super.getViewport().getRect().getWidth()/2) {
            return true;
        }
        return false;
    }

    private boolean is_backgroind_object_left_map(Animated animated) {
        return (animated.getPosition().getX()+
                animated.getCurrent_animation().getEssential_texture_scale().getX()/2) <
                super.getViewport().getRect().getLeft();
    }

    @Override
    public Vector<Sprite_for_loading> getSprites_for_loading() {
        Vector<Sprite_for_loading> result = new Vector<Sprite_for_loading>();

        result.add(new Sprite_for_loading(R.drawable.grass, new Rectangle (512,512), 1, 12f));
        result.add(new Sprite_for_loading(R.drawable.anim_bumblebee_fly, new Rectangle (180,220), 6, 2));
        //result.add(new Sprite_for_loading(R.drawable.balloon_red, new Rectangle (128,128), 1));
        result.add(new Sprite_for_loading(R.drawable.strawberry, new Rectangle (128,128), 1));
        result.add(new Sprite_for_loading(R.drawable.water, new Rectangle (256,32), 5, new Point(12,1)));

        return result;
    }

    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        super.onSurfaceChanged(glUnused, width, height);
        change_resolution(width, height);
        //update_watched_rect();
    }


    public void onAdLoaded() {

    }

    public void onAdClosed() {

    }


    public float getWaterHeight() {
        return water_height;
    }

    public void change_resolution(int screenWidth, int screenHeight) {
        getViewport().set_view_resolution(screenWidth, screenHeight);
        if (getViewport().getWatched_rect() == null) {
            try {
                getBumblebeeViewport().setWatch_upto_bottom(0);
            } catch (Exception e)  {
                assert(false);
            }
        }
    }
}