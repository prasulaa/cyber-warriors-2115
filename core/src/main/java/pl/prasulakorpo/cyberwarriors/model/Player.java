package pl.prasulakorpo.cyberwarriors.model;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.FrictionJoint;
import com.badlogic.gdx.physics.box2d.joints.FrictionJointDef;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import pl.prasulakorpo.cyberwarriors.drawing.Drawable;

import java.util.Objects;

import static pl.prasulakorpo.cyberwarriors.model.GameProperties.ERR;
import static pl.prasulakorpo.cyberwarriors.model.GameProperties.PPM;

@RequiredArgsConstructor
public class Player implements Drawable {

    @Getter
    private final String id;
    @Getter
    private final Fixture fixture;
    private final FrictionJoint frictionJoint;
    private final PlayerAnimations animations;
    @Getter
    private boolean directionLeft;
    @Getter
    @Setter
    private boolean onWall;

    @Override
    public Vector2 getPosition() {
        return fixture.getBody().getPosition();
    }

    @Override
    public TextureRegion getTextureRegion(float stateTime) {
        Vector2 velocity = fixture.getBody().getLinearVelocity();
        Animation<TextureRegion> animation;
        updateDirection(velocity);

        if (velocity.y > ERR) {
            animation = directionLeft ? animations.getJumpUpLeft() : animations.getJumpUpRight();
        } else if (velocity.y < -ERR) {
            animation = directionLeft ? animations.getJumpDownLeft() : animations.getJumpDownRight();
        } else if (Math.abs(velocity.x) > ERR) {
            animation = directionLeft ? animations.getRunLeft() : animations.getRunRight();
        } else {
            animation = directionLeft ? animations.getStandingPoseLeft() : animations.getStandingPoseRight();
        }

        return animation.getKeyFrame(stateTime);
    }

    public Vector2 getSize() {
        return new Vector2(0.5f, 0.5f);
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
