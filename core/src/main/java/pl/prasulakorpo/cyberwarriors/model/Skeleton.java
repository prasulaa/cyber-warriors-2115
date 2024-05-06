package pl.prasulakorpo.cyberwarriors.model;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Disposable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pl.prasulakorpo.cyberwarriors.drawing.Drawable;

import java.util.Arrays;
import java.util.Objects;

@AllArgsConstructor
public class Skeleton implements Drawable, Disposable {

    public enum Type {
        S1, S2
    }

    public static final float WIDTH = 0.64f;
    public static final float HEIGHT = 0.9f;

    @Getter
    private final Type type;
    @Getter
    private final Fixture fixture;
    private final Animation<TextureRegion> animation;
    private int healthPoints = 1;

    public void render() {
        fixture.getBody().setLinearVelocity(-1f, 0f);
    }

    @Override
    public Vector2 getPosition() {
        return fixture.getBody().getPosition();
    }

    @Override
    public TextureRegion getTextureRegion(float stateTime) {
        return animation.getKeyFrame(stateTime);
    }

    @Override
    public void dispose() {
        Arrays.stream(animation.getKeyFrames())
            .forEach(r -> r.getTexture().dispose());
    }

    public void hit(GameState gameState) {
        if (--healthPoints == 0) {
            dispose(gameState);
        }
    }

    public void dispose(GameState gameState) {
        Effect e = EffectFactory.skeletonDeath(fixture.getBody().getPosition(), gameState.getStateTime(), type.equals(Type.S1));
        gameState.getEffects().add(e);
        gameState.getDrawableManager().add(4, e);

        gameState.getSkeletons().remove(this);
        gameState.getDrawableManager().delete(this);
        gameState.getWorld().destroyBody(this.getFixture().getBody());
        dispose();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Skeleton skeleton = (Skeleton) o;
        return Objects.equals(fixture, skeleton.fixture);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fixture);
    }
}
