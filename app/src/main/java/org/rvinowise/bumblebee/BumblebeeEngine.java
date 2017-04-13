package org.rvinowise.bumblebee;

import android.content.Context;

import com.google.android.gms.ads.AdListener;

import org.rvinowise.bumblebee.units.Bumblebee;
import org.rvinowise.bumblebee.walls.Balloon;

import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;

import game.engine.Engine;
import game.engine.units.animation.Sprite_for_loading;
import game.engine.units.animation.Animated;
import game.engine.utils.primitives.Point;
import game.engine.utils.primitives.Rectangle;


public class BumblebeeEngine extends Engine
{


    public BumblebeeEngine() {

    }


    @Override
    public void init_scene() {


        getViewport().set_scale_of_shortest_side(5);

        Animated bumblebee = new Bumblebee();
        this.add_animated(bumblebee);
        bumblebee.startAnimation(this.getAnimations().get(0));
        bumblebee.setPosition(new Point(0, (float) 0.1));
        getAnimated().lastElement().setVector(new Point(0.01f, -0.00f));
        //animated.setDirection(330);

        getViewport().watch_object(bumblebee, new Point(-3,0));

        Animated balloon = new Balloon();
        this.add_animated(balloon);
        balloon.startAnimation(this.getAnimations().get(1));
        balloon.setPosition(new Point(2, (float) -0.1));
        getAnimated().lastElement().setVector(new Point(-0.01f, -0.01f));

        this.add_animated(new Balloon());
        getAnimated().lastElement().startAnimation(this.getAnimations().get(1));
        getAnimated().lastElement().setPosition(new Point(3, (float) 2));
        getAnimated().lastElement().setVector(new Point(0.004f, 0.00f));


        super.init_scene();
    }

    @Override
    public void step() {
        super.step();
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

    }

    public void onAdLoaded() {

    }

    public void onAdClosed() {

    }
}
