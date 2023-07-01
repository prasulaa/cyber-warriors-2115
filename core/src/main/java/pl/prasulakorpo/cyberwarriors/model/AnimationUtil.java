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

    public static class Paths {
        public static final String STANDING_POSE_LEFT = "STANDING_POSE_LEFT.png";
        public static final String STANDING_POSE_RIGHT = "STANDING_POSE_RIGHT.png";
        public static final String RUN_LEFT = "RUN_LEFT.png";
        public static final String RUN_RIGHT = "RUN_RIGHT.png";
        public static final String JUMP_UP_LEFT = "JUMP_UP_LEFT.png";
        public static final String JUMP_UP_RIGHT = "JUMP_UP_RIGHT.png";
        public static final String JUMP_DOWN_LEFT = "JUMP_DOWN_LEFT.png";
        public static final String JUMP_DOWN_RIGHT = "JUMP_DOWN_RIGHT.png";
    }
}
