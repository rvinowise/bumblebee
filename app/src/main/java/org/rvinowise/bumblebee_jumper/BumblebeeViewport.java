package org.rvinowise.bumblebee_jumper;


import org.rvinowise.bumblebee_jumper.units.Bumblebee;

import game.engine.Viewport;
import game.engine.units.animation.Animated;
import game.engine.utils.primitives.Rectangle;


public class BumblebeeViewport {
    Viewport viewport;
    Animated watched_object;

    float watch_upto_bottom;

    public BumblebeeViewport(Viewport in_viewport, Animated watched_animated) {
        viewport = in_viewport;
        watched_object = watched_animated;
    }

    public float getWatch_upto_bottom() {
        return watch_upto_bottom;
    }
    public void setWatch_upto_bottom(float watch_up_to_bottom) {
        watch_upto_bottom = watch_up_to_bottom;

        assert(viewport != null);
        assert(viewport.getRect() != null);

        viewport.setWatched_rect(
                new Rectangle(
                        viewport.getRect().getLeft() + (watched_object.getRadius() * 2),
                        viewport.getRect().getLeft() + (watched_object.getRadius() * 2) + 6,
                        watch_up_to_bottom,
                        viewport.getRect().getTop() - (watched_object.getRadius())
                ));
    }

}
