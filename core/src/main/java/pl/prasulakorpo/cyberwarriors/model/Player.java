package pl.prasulakorpo.cyberwarriors.model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.FrictionJoint;
import com.badlogic.gdx.physics.box2d.joints.FrictionJointDef;
import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.prasulakorpo.cyberwarriors.drawing.Drawable;

@AllArgsConstructor
public class Player implements Drawable {

    @Getter
    private final String id;
    @Getter
    private final Fixture fixture;
    private final FrictionJoint frictionJoint;

    @Override
    public Vector2 getPosition() {
        return fixture.getBody().getPosition();
    }

    @Override
    public TextureRegion getTextureRegion(float stateTime) {
        return null;
    }

}
