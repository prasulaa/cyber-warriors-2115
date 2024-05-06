package pl.prasulakorpo.cyberwarriors.model;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.FrictionJoint;
import com.badlogic.gdx.utils.Disposable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import pl.prasulakorpo.cyberwarriors.drawing.Drawable;

import static pl.prasulakorpo.cyberwarriors.GameProperties.ERR;

@RequiredArgsConstructor
public class Player implements Drawable, Disposable {

    public static final float WIDTH = 0.64f;
    public static final float HEIGHT = 0.9f;

    @Getter
    private final String id;
    @Getter
    private final Fixture fixture;
    @Getter
    private final FrictionJoint frictionJoint;
    private final PlayerAnimations animations;
    @Setter
    private boolean directionLeft;
    @Getter
    @Setter
    private float lastTimeAttacked;

    @Override
    public Vector2 getPosition() {
        return fixture.getBody().getPosition();
    }

    @Override
    public TextureRegion getTextureRegion(float stateTime) {
        Vector2 velocity = fixture.getBody().getLinearVelocity();
        Animation<TextureRegion> animation;
        updateDirection(velocity);

        if (stateTime - lastTimeAttacked < 0.36f) {
            animation = directionLeft ? animations.getAttackLeft() : animations.getAttackRight();
            stateTime = stateTime - lastTimeAttacked;
        } else if (Math.abs(velocity.x) + Math.abs(velocity.y) > ERR) {
            animation = directionLeft ? animations.getRunLeft() : animations.getRunRight();
        } else {
            animation = directionLeft ? animations.getStandingPoseLeft() : animations.getStandingPoseRight();
        }

        return animation.getKeyFrame(stateTime);
    }

    public boolean attackOverlaps(Skeleton s) {
        final float attackWidth = 0.8f;
        final float attackHeight = 1.4f;

        float x;
        if (directionLeft) {
            x = - (Player.WIDTH/2 + attackWidth);
        } else {
            x = Player.WIDTH/2;
        }

        Vector2 playerPos = fixture.getBody().getPosition();
        Rectangle attackRectangle = new Rectangle(playerPos.x + x , playerPos.y - attackHeight/2, attackWidth, attackHeight);

        Vector2 skeletonPos = s.getFixture().getBody().getPosition();
        Rectangle skeletonRectangle = new Rectangle(skeletonPos.x - Skeleton.WIDTH/2, skeletonPos.y - Skeleton.HEIGHT/2, Skeleton.WIDTH, Skeleton.HEIGHT);

        return attackRectangle.overlaps(skeletonRectangle);
    }

    public boolean isAttackCooldown(float stateTime) {
        return stateTime - lastTimeAttacked < 0.45f;
    }

    @Override
    public void dispose() {
        animations.dispose();
    }

    private void updateDirection(Vector2 velocity) {
        if (velocity.x < -ERR) {
            directionLeft = true;
        } else if (velocity.x > ERR) {
            directionLeft = false;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return id.equals(((Player) o).getId());
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
