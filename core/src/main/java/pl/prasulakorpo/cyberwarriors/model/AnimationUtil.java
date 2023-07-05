package pl.prasulakorpo.cyberwarriors.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimationUtil {

    public static Animation<TextureRegion> load(String path, float frameDuration, int framesNum, Animation.PlayMode playMode) {
        Texture walkSheet = new Texture(Gdx.files.internal(path));
        TextureRegion[] walkFrames = TextureRegion.split(walkSheet, walkSheet.getWidth() / framesNum, walkSheet.getHeight())[0];
        Animation<TextureRegion> animation = new Animation<>(frameDuration, walkFrames);
        animation.setPlayMode(playMode);
        return animation;
    }

}
