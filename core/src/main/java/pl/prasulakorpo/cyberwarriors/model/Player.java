package pl.prasulakorpo.cyberwarriors.model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.joints.FrictionJoint;
import lombok.AllArgsConstructor;
import pl.prasulakorpo.cyberwarriors.drawing.Drawable;

@AllArgsConstructor
public class Player implements Drawable {

    private final Fixture fixture;
    private final FrictionJoint frictionJoint;

    @Override
    public Vector2 getPosition() {
        return null;
    }

    @Override
    public TextureRegion getTextureRegion(float stateTime) {
        return null;
    }

    public Fixture getFixture() {
        return fixture;
    }

}
