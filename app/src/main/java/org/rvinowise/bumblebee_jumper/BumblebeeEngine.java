package org.rvinowise.bumblebee_jumper;

import org.rvinowise.bumblebee_jumper.units.Bumblebee;
import org.rvinowise.bumblebee_jumper.walls.Balloon;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;

import game.engine.Engine;
import game.engine.pos_functions.pos_functions;
import game.engine.units.animation.Animation;
import game.engine.units.animation.Effect;
import game.engine.units.animation.Sprite_for_loading;
import game.engine.units.animation.Animated;
import game.engine.utils.primitives.Point;
import game.engine.utils.primitives.Rectangle;


public class BumblebeeEngine extends Engine
{

    private Set<Balloon> balloons = new HashSet<Balloon>();
    private Bumblebee bumblebee;

    private Units_generator generator = new Units_generator(this);

    public Collection<Balloon> getBalloons() {
        return balloons;
    }

    public BumblebeeViewport getBumblebeeViewport() {
        return bumblebee_viewport;
    }

    BumblebeeViewport bumblebee_viewport;


    public BumblebeeEngine() {

    }


    /* инициализировать сцену.
       создать и настроить контролирующие объекты (типа View),
       на основе которых будут генерироваться новые объекты в процессе движения игры.
            При изменении важных характеристик (соотношение сторон монитора),
       эти контролирующие объекты изменятся/подстроятся      */

    @Override
    public void init_scene() {
        bumblebee = new Bumblebee();
        init_viewport(bumblebee); //RV тут создается bumblebeeViewport который нужен при onSurfaceChanged

        bumblebee.startAnimation(Animation.valueOf(R.drawable.anim_bumblebee_fly));
        bumblebee.setPosition(new Point(0, (float) 0.1));



        Animated strawberry = add_strawberry();
        strawberry.setPosition(bumblebee.getPosition().plus(new Point(4, -2)));

        strawberry = add_strawberry();
        strawberry.setPosition(bumblebee.getPosition().plus(new Point(7, -3)));


        generator.init_scene();

        /*Effect.create(Animation.valueOf(R.drawable.anim_bumblebee_fly),
                bumblebee.getPosition().plus(new Point(2,0)),
                        0);*/


        super.register_first_step_as_done();
    }

    private void init_viewport(Animated watched_object) {

        bumblebee_viewport = new BumblebeeViewport(super.getViewport(), watched_object);
        super.getViewport().watch_object(watched_object);
    }


    private boolean is_resolution_known() {
        return (getViewport().getRect() != null);
    }

    public Balloon add_strawberry() {
        Balloon balloon = new Balloon();
        balloons.add(balloon);
        return balloon;
    }
    protected void remove(int i_physical) {
        Animated physical = getAnimateds().get(i_physical);
        if (physical instanceof Balloon) {
            balloons.remove(physical);
        }

        super.remove(i_physical);
    }

    @Override
    public void step() {
        player_control();
        process_player_physics();
        process_maybe_changing_viewport();
        process_elementary_physics();


        generator.step();
    }

    private void process_maybe_changing_viewport() {


        final float water_y = generator.getWater_y();
        float distance_to_water = bumblebee.getPosition().getY() - water_y;
        if (
                (getBumblebeeViewport().getWatch_upto_bottom() == 0) &&
                        (distance_to_water < super.getViewport().getRect().getHeight() / 2)
                ) {
            getBumblebeeViewport().setWatch_upto_bottom(super.getViewport().getRect().getBottom());
        } else if (
                (getBumblebeeViewport().getWatch_upto_bottom() < 0) &&
                        (distance_to_water >= super.getViewport().getRect().getHeight() / 2)
                ) {
            getBumblebeeViewport().setWatch_upto_bottom(0);
        }

    }

    private void process_elementary_physics() {
        super.step();
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

        if (bumblebee.getPosition().getY() <= getWaterHeight()) {
            Effect effect = Effect.create(Animation.valueOf(R.drawable.water_splash),
                    new Point(bumblebee.getPosition().getX(), getWaterHeight()), 0);
            effect.setAnimation_speed(0.5f);
            bumblebee.setVector(new Point(
                    bumblebee.getVector().getX(),
                    -bumblebee.getVector().getY()));
        }
    }

    private void player_jump_from_balloons(Collection<Animated> collided) {
        for (Animated animated: collided) {
            if (animated instanceof Balloon) {
                jump_from_balloon(bumblebee, (Balloon)animated);
            }
        }
    }

    private void jump_from_balloon(Animated jumper, Balloon from) {
        final Point prev_position = jumper.getPosition().minus(jumper.getVector());
        final float dir_to_balloon = pos_functions.poidir(prev_position, from.getPosition());
        float corner =pos_functions.corner(jumper.getVectorDirection(), dir_to_balloon);
        float bounce_dir = (dir_to_balloon-180) + corner;
        float bounce_speed = jumper.getVectorLength();
        jumper.setVector(pos_functions.lendir(bounce_speed, bounce_dir));
    }


    private void player_control() {
        if (getControl().isTouched()) {
            bumblebee.rush(90);
        }
    }

    @Override
    protected boolean no_need_more(Animated animted) {
        if (super.no_need_more(animted)) {
            return true;
        }
        return is_left_map(animted);
    }

    private boolean is_left_map(Animated animted) {

        if (
                (animted.getPosition().getX()+
                        animted.getCurrent_animation().getEssential_texture_scale().getX()/2) <
            super.getViewport().getRect().getLeft()) {
            return true;
        }
        return false;
    }


    @Override
    public Vector<Sprite_for_loading> getSprites_for_loading() {
        Vector<Sprite_for_loading> result = new Vector<Sprite_for_loading>();

        result.add(new Sprite_for_loading(R.drawable.grass, new Rectangle (512,512), 1, 12f));
        result.add(new Sprite_for_loading(R.drawable.anim_bumblebee_fly, new Rectangle (160,220), 6, 2, new Point(0, 0)));
        result.add(new Sprite_for_loading(R.drawable.strawberry, new Rectangle (128,128), 1));
        result.add(new Sprite_for_loading(R.drawable.water, new Rectangle (256,32), 5, new Point(12,1)));
        result.add(new Sprite_for_loading(R.drawable.water_splash, new Rectangle(62,62), 10, new Point(2,2), new Point(0, 19)));

        return result;
    }

    boolean scene_created = false;
    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        super.getViewport().set_scale_of_shortest_side(7);
        super.onSurfaceChanged(glUnused, width, height);
        if (!scene_created) {
            init_scene();
            getBumblebeeViewport().setWatch_upto_bottom(0);
        }
        //change_resolution(width, height);
        //update_watched_rect();
    }


    public void onAdLoaded() {

    }

    public void onAdClosed() {

    }


    public float getWaterHeight() {
        return generator.getWater_y();
    }

    public void change_resolution(int screenWidth, int screenHeight) {
        getBumblebeeViewport().setWatch_upto_bottom(0);

    }
}
