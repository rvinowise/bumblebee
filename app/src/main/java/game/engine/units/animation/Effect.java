package game.engine.units.animation;


import game.engine.utils.primitives.Point;

public class Effect extends Animated {

    public static Effect create(Animation animation, Point position, float direction) {
        Effect effect = new Effect();
        effect.position = position;
        effect.direction = direction;
        effect.startAnimation(animation);

        return effect;
    }
}
