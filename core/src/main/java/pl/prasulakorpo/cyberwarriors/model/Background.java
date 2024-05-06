package pl.prasulakorpo.cyberwarriors.model;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import pl.prasulakorpo.cyberwarriors.drawing.Drawable;

import static pl.prasulakorpo.cyberwarriors.GameProperties.*;

public class Background implements Drawable {

    private final Animation<TextureRegion> animation;

    public Background() {
        animation = AnimationUtil.load(TexturePaths.MAP, 0.15f, 7, Animation.PlayMode.LOOP);
    }

    @Override
    public Vector2 getPosition() {
        return new Vector2(WIDTH/PPM/2, HEIGHT/PPM/2);
    }

    @Override
    public TextureRegion getTextureRegion(float stateTime) {
        return animation.getKeyFrame(stateTime);
    }
}
