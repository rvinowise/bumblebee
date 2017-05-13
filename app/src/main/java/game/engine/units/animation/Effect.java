package game.engine.units.animation;


import game.engine.utils.primitives.Point;

public class Effect extends Animated {



    private boolean deleted = false;

    public static Effect create(Animation animation, Point position, float direction) {
        Effect effect = new Effect();
        effect.position = position;
        effect.direction = direction;
        effect.startAnimation(animation);

        return effect;
    }

    @Override
    protected void set_next_frame() {
        current_frame ++;
        if (current_frame >= current_animation.getFrames_qty()) {
            this.remove();
        } else {
            current_animation.setMatrix_to_next_frame(texture_matrix, (int)Math.floor(current_frame));
        }
    }

}
