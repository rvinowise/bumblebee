package org.rvinowise.bumblebee_jumper;


import org.rvinowise.bumblebee_jumper.background.Backgrownd;
import org.rvinowise.bumblebee_jumper.walls.Balloon;

import java.util.Collection;
import java.util.Random;

import game.engine.units.animation.Animation;
import game.engine.utils.primitives.Point;

public class Units_generator {

    private final BumblebeeEngine engine;
    final Random random = new Random(0);
    private Backgrownd water_flow = new Backgrownd();
    private Backgrownd grass_flow = new Backgrownd();

    public Units_generator(BumblebeeEngine in_engine) {
        engine = in_engine;
    }

    public void init_scene() {
        water_flow.init(engine.getViewport(), Animation.valueOf(R.drawable.water),
                (Collection)engine.getAnimateds());
        grass_flow.init(engine.getViewport(), Animation.valueOf(R.drawable.grass),
                (Collection)engine.getAnimateds());

        water_flow.create_first_instances(-4);
        grass_flow.create_first_instances(water_flow.getLast_instance().getPosition().getY()+
                grass_flow.getAnimation().getEssential_texture_scale().getY()/2);

    }


    public void step() {
        if (too_few_strawberries()) {
            create_strawberry_ahead();
        }
        if (water_flow.no_more_instances_ahead()) {
            water_flow.prolongate();
        }
        if (grass_flow.no_more_instances_ahead()) {
            grass_flow.prolongate();
        }
        water_flow.step_instances();
        grass_flow.step_instances();
    }



    private boolean too_few_strawberries() {
        if (engine.getBalloons().size() < 7) {
            return true;
        }
        return false;
    }

    private void create_strawberry_ahead() {
        float line_ahead = engine.getViewport().getRect().getRight()+ Balloon.getStandardRadius();
        float random_height = engine.getViewport().getRect().getBottom()+
                random.nextInt((int) (engine.getViewport().getRect().getHeight()+1 - Balloon.getStandardRadius()*2));
        Balloon balloon = engine.add_strawberry();
        balloon.setPosition(new Point(line_ahead, random_height));
    }


    public float getWater_y() {
        return water_flow.getLast_instance().getPosition().getY();
    }
}
