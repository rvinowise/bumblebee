package org.rvinowise.bumblebee_jumper.background;


import java.util.Collection;

import game.engine.Viewport;
import game.engine.units.animation.Animated;
import game.engine.units.animation.Animation_type;

public class Grass extends Backgrownd {

    public static Animation_type getAnimation() {
        return animation;
    }

    protected static Animation_type animation;



    public static void init(Viewport in_viewport, Animation_type in_animation,
                            Collection<Animated> in_engine_animated) {
        animation = in_animation;
        Backgrownd.init(in_viewport, in_animation, in_engine_animated);
    }

}
