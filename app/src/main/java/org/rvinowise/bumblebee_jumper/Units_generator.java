package org.rvinowise.bumblebee_jumper;


import org.rvinowise.bumblebee_jumper.background.Backgrownd;
import org.rvinowise.bumblebee_jumper.walls.Strawberry;

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
        water_flow.init(engine.getViewport(), Animation.valueOf(R.drawable.water), new Point(1,1),
                (Collection)engine.getAnimateds());
        water_flow.setAnimation_speed(0.6f);

        grass_flow.init(engine.getViewport(), Animation.valueOf(R.drawable.grass), new Point(12,12),
                (Collection)engine.getAnimateds());

        water_flow.create_first_instances(-4);
        grass_flow.create_first_instances(water_flow.getLast_instance().getPosition().getY()+
                grass_flow.getHeight()/2);

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
        if (engine.getStrawberries().size() < 7) {
            return true;
        }
        return false;
    }

    private void create_strawberry_ahead() {
        float line_ahead = engine.getViewport().getRect().getRight()+ Strawberry.getStandardRadius();
        float random_height = engine.getViewport().getRect().getBottom()+
                random.nextFloat()*engine.getViewport().getRect().getHeight() - Strawberry.getStandardRadius();
        Strawberry strawberry = engine.add_strawberry(new Point(line_ahead, random_height));
        //strawberry.setPosition(new Point(line_ahead, random_height));
    }


    public float getWater_y() {
        return water_flow.getLast_instance().getPosition().getY();
    }
}
