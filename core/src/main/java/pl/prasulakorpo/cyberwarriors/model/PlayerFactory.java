package pl.prasulakorpo.cyberwarriors.model;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.FrictionJoint;
import com.badlogic.gdx.physics.box2d.joints.FrictionJointDef;

public class PlayerFactory {

    public static Player create(String id, float x, float y, GameState gameState) {
        Body body = createBody(x, y, gameState.getWorld());
        body.setFixedRotation(true);

        return new Player(id,
            createFixture(body),
            createFrictionJoint(body, gameState.getBackground().getBody(), gameState.getWorld()),
            loadAnimations());
    }

    private static Body createBody(float x, float y, World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);

        return world.createBody(bodyDef);
    }

    private static Fixture createFixture(Body body) {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.5f, 0.5f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;

        Fixture fixture = body.createFixture(fixtureDef);
        shape.dispose();
        return fixture;
    }

    private static FrictionJoint createFrictionJoint(Body body1, Body body2, World world) {
        FrictionJointDef jointDef = new FrictionJointDef();
        jointDef.maxForce = 1f;
        jointDef.maxTorque = 1f;
        jointDef.initialize(body1, body2, new Vector2(0, 0));
        return (FrictionJoint) world.createJoint(jointDef);
    }

    private static PlayerAnimations loadAnimations() {
        return new PlayerAnimations(
            AnimationUtil.load(AnimationUtil.Paths.STANDING_POSE_LEFT, 0.25f, 1, Animation.PlayMode.LOOP),
            AnimationUtil.load(AnimationUtil.Paths.STANDING_POSE_RIGHT, 0.25f, 1, Animation.PlayMode.LOOP),
            AnimationUtil.load(AnimationUtil.Paths.RUN_LEFT, 0.25f, 4, Animation.PlayMode.LOOP),
            AnimationUtil.load(AnimationUtil.Paths.RUN_RIGHT, 0.25f, 4, Animation.PlayMode.LOOP),
            AnimationUtil.load(AnimationUtil.Paths.JUMP_UP_LEFT, 0.5f, 2, Animation.PlayMode.NORMAL),
            AnimationUtil.load(AnimationUtil.Paths.JUMP_UP_RIGHT, 0.5f, 2, Animation.PlayMode.NORMAL),
            AnimationUtil.load(AnimationUtil.Paths.JUMP_DOWN_LEFT, 0.25f, 1, Animation.PlayMode.NORMAL),
            AnimationUtil.load(AnimationUtil.Paths.JUMP_DOWN_RIGHT, 0.25f, 1, Animation.PlayMode.NORMAL)
        );
    }

}
