package org.rvinowise.bumblebee_jumper;


import org.rvinowise.bumblebee_jumper.units.Bumblebee;

import game.engine.Viewport;
import game.engine.utils.primitives.Rectangle;


public class BumblebeeViewport {
    Viewport viewport;
    Bumblebee bumblebee;

    float watch_upto_bottom;

    public BumblebeeViewport(Viewport in_viewport, Bumblebee in_bumblebee) {
        viewport = in_viewport;
        bumblebee = in_bumblebee;
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
                        viewport.getRect().getLeft() + (bumblebee.getRadius() * 2),
                        viewport.getRect().getLeft() + (bumblebee.getRadius() * 2) + 6,
                        watch_up_to_bottom,
                        viewport.getRect().getTop() - (bumblebee.getRadius())
                ));
    }

}
