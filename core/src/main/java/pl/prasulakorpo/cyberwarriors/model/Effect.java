package pl.prasulakorpo.cyberwarriors.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import lombok.RequiredArgsConstructor;
import pl.prasulakorpo.cyberwarriors.drawing.Drawable;

import java.util.Objects;

@RequiredArgsConstructor
public class Effect implements Drawable {

    private final Vector2 position;
    private final Animation<TextureRegion> animation;
    private final Animation<TextureRegion> stillAnimation;
    private final float stillAnimationTime;
    private final float creationTime;

    @Override
    public Vector2 getPosition() {
        return position;
    }

    @Override
    public TextureRegion getTextureRegion(float stateTime) {
        float time = (stateTime - creationTime);
        float x = time / (animation.getAnimationDuration() + stillAnimationTime);

        if (x - Math.floor(x) < stillAnimationTime / (animation.getAnimationDuration() + stillAnimationTime)) {
            return stillAnimation.getKeyFrame(stateTime - creationTime);
        }
        return animation.getKeyFrame(time - (float) (Math.floor(x) * (animation.getAnimationDuration() + stillAnimationTime)) - stillAnimationTime);
    }

    public void render(GameState gameState) {
        if (stillAnimation != null) {
            return;
        }

        Effect thisEffect = this;
        if (isFinished(gameState.getStateTime())) {
            Gdx.app.postRunnable(() -> {
                gameState.getEffects().remove(thisEffect);
                gameState.getDrawableManager().delete(thisEffect);
            });
        }
    }

    private boolean isFinished(float stateTime) {
        return animation.isAnimationFinished(stateTime - creationTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Effect effect = (Effect) o;
        return Float.compare(creationTime, effect.creationTime) == 0 && Objects.equals(position, effect.position) && Objects.equals(animation, effect.animation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, animation, creationTime);
    }
}
