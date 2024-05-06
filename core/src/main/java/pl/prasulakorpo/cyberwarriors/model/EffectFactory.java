package pl.prasulakorpo.cyberwarriors.model;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;

import static pl.prasulakorpo.cyberwarriors.model.TexturePaths.*;

public class EffectFactory {
    public static Effect skeletonDeath(Vector2 position, float stateTime, boolean s1) {
        return new Effect(
            position,
            s1 ? AnimationUtil.load(S1_DEATH_LEFT, 0.15f,11, Animation.PlayMode.NORMAL)
                : AnimationUtil.load(S2_DEATH_LEFT, 0.15f,13, Animation.PlayMode.NORMAL),
            null,
            0f,
            stateTime);
    }

    public static Effect birds(Vector2 position, float stateTime) {
        return new Effect(
            position,
            AnimationUtil.load(BIRDS, 0.1f, 7, Animation.PlayMode.LOOP),
            AnimationUtil.load(MAP, 0.15f, 7, Animation.PlayMode.LOOP),
            5f,
            stateTime
        );
    }
}
