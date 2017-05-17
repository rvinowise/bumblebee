package org.rvinowise.bumblebee_jumper;


import android.util.Log;

import org.rvinowise.bumblebee_jumper.background.Backgrownd;
import org.rvinowise.bumblebee_jumper.walls.Strawberry;

import java.sql.Time;
import java.util.Collection;
import java.util.Random;

import game.engine.units.animation.Animation;
import game.engine.utils.primitives.Point;

public class Units_generator {

    private final BumblebeeEngine engine;
    private final Random random;
    private Backgrownd water_flow = new Backgrownd();
    private Backgrownd grass_flow = new Backgrownd();

    Units_generator(BumblebeeEngine in_engine) {
        engine = in_engine;

        long random_seed = System.currentTimeMillis();
        random = new Random(random_seed);
        Log.d("Units_generator", "random_seed = "+random_seed);
    }

    void init_scene() {
        water_flow.init(engine.getViewport(), Animation.valueOf(R.drawable.water), new Point(1,1));
        water_flow.setAnimation_speed(0.6f);

        grass_flow.init(engine.getViewport(), Animation.valueOf(R.drawable.grass), new Point(12,12));

        water_flow.create_first_instances(-4);
        grass_flow.create_first_instances(water_flow.getLast_instance().getPosition().getY()+
                grass_flow.getHeight()/2);

    }


    void step() {
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

        float radius = Strawberry.get_random_radius(random);
        //float radius = 0.5f;

        float line_ahead = engine.getViewport().getRect().getRight()+ radius;
        float random_height = engine.getViewport().getRect().getBottom()+
                random.nextFloat()*engine.getViewport().getRect().getHeight() - Strawberry.getStandardRadius();
        Strawberry strawberry = engine.add_strawberry(new Point(line_ahead, random_height));

        strawberry.setRadius(radius);

        //strawberry.setPosition(new Point(line_ahead, random_height));
    }


    float getWater_y() {
        return water_flow.getLast_instance().getPosition().getY();
    }
}
