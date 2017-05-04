/*package org.rvinowise.bumblebee_jumper;


import org.rvinowise.bumblebee_jumper.units.Bumblebee;

import java.util.Collection;

import game.engine.units.animation.Animated;
import game.engine.units.animation.Animation;
import game.engine.units.animation.Effect;
import game.engine.utils.primitives.Point;

public class Player_control {
    Bumblebee bumblebee;
    BumblebeeEngine engine;

    static Player_control instance;

    private Player_control(Bumblebee bumblebee, BumblebeeEngine engine) {
        this.bumblebee = bumblebee;
        this.engine = engine;
    }

    static public  void init(Bumblebee bumblebee, BumblebeeEngine engine) {
        instance = new Player_control(bumblebee, engine);
    }

    static public Player_control getInstance() {
        return instance;
    }

    private void process_player_physics() {
        final Point gravity_vector = new Point(0f,-0.001f);
        bumblebee.setVector(bumblebee.getVector().plus(gravity_vector));

        final Point go_forward_vector = new Point(0.01f,0f);
        if (bumblebee.getVector().getX() < 0.1) {
            bumblebee.setVector(bumblebee.getVector().plus(go_forward_vector));
        }

        Collection<Animated> collided = getCollided_circle(bumblebee);
        player_jump_from_balloons(collided);

        if (bumblebee.getPosition().getY()-bumblebee.getRadius() <= getWaterHeight()) {
            Effect effect = Effect.create(Animation.valueOf(R.drawable.water_splash),
                    new Point(bumblebee.getPosition().getX(), getWaterHeight()), 0);
            effect.setAnimation_speed(0.5f);
            effect.setSize(new Point(bumblebee.getRadius()*6, Math.abs(bumblebee.getVector().getY())*6));
            bumblebee.setVector(new Point(
                    bumblebee.getVector().getX(),
                    -bumblebee.getVector().getY()));
        }
    }

}*/
