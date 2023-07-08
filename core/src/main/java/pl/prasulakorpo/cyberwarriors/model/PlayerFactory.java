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

        Player player = new Player(id,
            createFixture(body),
            createFrictionJoint(body, gameState.getBackground().getFixture().getBody(), gameState.getWorld()),
            loadAnimations());

        player.getFixture().setUserData(player);

        return player;
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
        fixtureDef.friction = 0.07f;

        Fixture fixture = body.createFixture(fixtureDef);
        shape.dispose();
        return fixture;
    }

    private static FrictionJoint createFrictionJoint(Body body1, Body body2, World world) {
        FrictionJointDef jointDef = new FrictionJointDef();
        jointDef.maxForce = 10f;
        jointDef.maxTorque = 10f;
        jointDef.initialize(body1, body2, new Vector2(0, 0));
        return (FrictionJoint) world.createJoint(jointDef);
    }

    private static PlayerAnimations loadAnimations() {
        return new PlayerAnimations(
            AnimationUtil.load(TexturePaths.STANDING_POSE_LEFT, 0.25f, 1, Animation.PlayMode.LOOP),
            AnimationUtil.load(TexturePaths.STANDING_POSE_RIGHT, 0.25f, 1, Animation.PlayMode.LOOP),
            AnimationUtil.load(TexturePaths.RUN_LEFT, 0.25f, 4, Animation.PlayMode.LOOP),
            AnimationUtil.load(TexturePaths.RUN_RIGHT, 0.25f, 4, Animation.PlayMode.LOOP),
            AnimationUtil.load(TexturePaths.JUMP_UP_LEFT, 0.5f, 2, Animation.PlayMode.NORMAL),
            AnimationUtil.load(TexturePaths.JUMP_UP_RIGHT, 0.5f, 2, Animation.PlayMode.NORMAL),
            AnimationUtil.load(TexturePaths.JUMP_DOWN_LEFT, 0.25f, 1, Animation.PlayMode.NORMAL),
            AnimationUtil.load(TexturePaths.JUMP_DOWN_RIGHT, 0.25f, 1, Animation.PlayMode.NORMAL)
        );
    }

}
