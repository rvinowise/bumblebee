package game.engine.initialisation;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

import java.util.Vector;

import game.engine.units.animation.Animation;
import game.engine.units.animation.Sprite_for_loading;

import static android.opengl.GLES20.GL_LINEAR;
import static android.opengl.GLES20.GL_NEAREST;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glGenTextures;
import static android.opengl.GLES20.glTexParameteri;


public class Sprite_loader {

    private Vector<Sprite_for_loading> background_sprites = new Vector<Sprite_for_loading>();
    private Vector<Sprite_for_loading> foreground_sprites = new Vector<Sprite_for_loading>();

    Context context;

    enum Sprite_place {
        Background, Foreground
    }
    Sprite_place current_sprite_place;

    public Sprite_loader() {

    }

    public void start_background_registration() {
        current_sprite_place = Sprite_place.Background;
    }
    public void start_foreground_registration() {
        current_sprite_place = Sprite_place.Foreground;
    }

    public void add(Sprite_for_loading sprite) {
        if (current_sprite_place == Sprite_place.Background) {
            background_sprites.add(sprite);
        } else if (current_sprite_place == Sprite_place.Foreground) {
            foreground_sprites.add(sprite);
        } else {
            throw new RuntimeException("add sprite without determined place");
        }
    }


    public Vector<Sprite_for_loading> getBackground_sprites() {
        return background_sprites;
    }

    public Vector<Sprite_for_loading> getForeground_sprites() {
        return foreground_sprites;
    }

    public void setContext(Context context) {
        this.context = context;
    }
    public void load_sprites_to_animations(Vector<Sprite_for_loading> spprites_from, Vector<Animation> animations_to) {
        for (Sprite_for_loading sprite : spprites_from) {
            Bitmap bmp = load_bitmap_for_sprite(context, sprite);

            Animation animation = new Animation(
                    bmp, sprite);
            glGenTextures(1, animation.getTexture().getHandleRef(), 0);
            if (animation.getTexture().getHandle() == 0) {
                throw new RuntimeException("can't create texture");
            }


            glBindTexture(GL_TEXTURE_2D, animation.getTexture().getHandle());
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            GLUtils.texImage2D(GL_TEXTURE_2D, 0, bmp, 0);
            bmp.recycle();

            animations_to.add(animation);
        }
    }
    private Bitmap load_bitmap_for_sprite(Context context, Sprite_for_loading sprite) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        Bitmap bmp = BitmapFactory.decodeResource(
                context.getResources(), sprite.getResource_id(), options);
        return bmp;
    }
}
