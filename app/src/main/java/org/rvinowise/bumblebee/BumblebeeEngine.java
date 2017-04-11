package org.rvinowise.bumblebee;

import android.content.Context;

import com.google.android.gms.ads.AdListener;

import org.rvinowise.bumblebee.units.Bumblebee;

import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;

import game.engine.Engine;
import game.engine.units.animation.Sprite_for_loading;
import game.engine.units.animation.Animated;
import game.engine.utils.primitives.Point;


public class BumblebeeEngine extends Engine
{


    public BumblebeeEngine() {
    }


    @Override
    public void init_scene() {
        getViewport().set_scale_of_shortest_side(20);

        Animated animated = new Bumblebee();
        this.add_animated(animated);
        animated.startAnimation(this.getAnimations().get(0));
        animated.setPosition(new Point(0, (float) -0.1));
        //animated.setDirection(330);
    }

    @Override
    public void step() {
        super.step();
    }

    @Override
    public Vector<Sprite_for_loading> getSprites_for_loading() {
        Vector<Sprite_for_loading> result = new Vector<Sprite_for_loading>();
        result.add(new Sprite_for_loading(R.drawable.anim_bumblebee_fly, 3, 2, 6));
        result.add(new Sprite_for_loading(R.drawable.anim_bumblebee_fly, 3, 2, 4));
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
