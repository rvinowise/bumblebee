package org.rvinowise.bumblebee_jumper;


import org.rvinowise.bumblebee_jumper.walls.Balloon;
import org.rvinowise.bumblebee_jumper.background.Water;
import org.rvinowise.bumblebee_jumper.background.Grass;

import java.util.Random;

import game.engine.utils.primitives.Point;

public class Units_generator {

    private final BumblebeeEngine engine;
    final Random random = new Random(0);

    public Units_generator(BumblebeeEngine in_engine) {
        engine = in_engine;
    }

    public void init_scene() {
        Water.create_first_instances(-4);
        Grass.create_first_instances(Water.getLast_instance().getPosition().getY()+
                Water.getAnimation().getEssential_texture_scale().getY()/2);

    }


    public void step() {
        if (too_few_balloons()) {
            create_balloon_ahead();
        }
        if (Water.no_more_instances_ahead()) {
            Water.prolongate();
        }
        if (Grass.no_more_instances_ahead()) {
            Grass.prolongate();
        }
    }



    private boolean too_few_balloons() {
        if (engine.getBalloons().size() < 7) {
            return true;
        }
        return false;
    }

    private void create_balloon_ahead() {
        float line_ahead = engine.getViewport().getRect().getRight()+ Balloon.getStandardRadius();
        float random_height = engine.getViewport().getRect().getBottom()+
                random.nextInt((int) (engine.getViewport().getRect().getHeight()+1 - Balloon.getStandardRadius()*2));
        Balloon balloon = engine.add_balloon();
        balloon.setPosition(new Point(line_ahead, random_height));
    }


}
