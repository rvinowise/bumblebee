package org.rvinowise.bumblebee;

import android.view.MotionEvent;
import android.view.View;

import org.rvinowise.bumblebee.units.Bumblebee;
import org.rvinowise.bumblebee.walls.Balloon;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;

import game.engine.Engine;
import game.engine.units.Physical;
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

    public BumblebeeEngine() {

    }


    @Override
    public void init_scene() {


        getViewport().set_scale_of_shortest_side(5);

        bumblebee = new Bumblebee();
        this.add_physical(bumblebee);
        bumblebee.startAnimation(this.getAnimations().get(0));
        bumblebee.setPosition(new Point(0, (float) 0.1));
        getPhysicals().lastElement().setVector(new Point(0.1f, -0.00f));
        //animated.setDirection(330);

        getViewport().watch_object(bumblebee, new Point(-3,0));

        Animated balloon = add_balloon();
        balloon.setPosition(new Point(2, (float) -0.1));
        getPhysicals().lastElement().setVector(new Point(-0.01f, -0.01f));


        balloon = add_balloon();
        balloon.setPosition(new Point(3, (float) 2));
        balloon.setVector(new Point(0.004f, 0.00f));


        super.init_scene();
    }

    public Balloon add_balloon() {
        Balloon balloon = new Balloon();
        super.add_physical(balloon);
        balloons.add(balloon);
        balloon.startAnimation(this.getAnimations().get(1));
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
        process_units_physics();
        super.step();
        generator.step();
    }

    private void process_units_physics() {
        final Point gravity_vector = new Point(0f,-0.001f);
        bumblebee.setVector(bumblebee.getVector().plus(gravity_vector));
    }

    private void player_control() {
        if (getControl().isTouched()) {
            bumblebee.rush(-90);
        }
    }

    @Override
    protected boolean no_need_more(Physical physical) {
        return is_left_map(physical);
    }

    private boolean is_left_map(Physical physical) {
        if (physical.getPosition().getX() < -getViewport().getRect().getWidth()/2) {
            return true;
        }
        return false;
    }

    @Override
    public Vector<Sprite_for_loading> getSprites_for_loading() {
        Vector<Sprite_for_loading> result = new Vector<Sprite_for_loading>();
        result.add(new Sprite_for_loading(R.drawable.anim_bumblebee_fly_real, new Rectangle (32,32), 6));
        result.add(new Sprite_for_loading(R.drawable.balloon_red, new Rectangle (128,128), 1));
        return result;
    }

    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        super.onSurfaceChanged(glUnused, width, height);
        getViewport().setWatched_pos(new Point(
                getViewport().getRect().getLeft()/2,
                0
        ));

    }

    public void onAdLoaded() {

    }

    public void onAdClosed() {

    }



}
