package pl.prasulakorpo.cyberwarriors.model;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.FrictionJoint;
import com.badlogic.gdx.physics.box2d.joints.FrictionJointDef;

public class PlayerFactory {

    public static Player create(String id, float x, float y, boolean p1, GameState gameState) {
        Body body = createBody(x, y, gameState.getWorld());
        body.setFixedRotation(true);

        Player player = new Player(id,
            createFixture(body),
//            createAttackFixture(x, y, gameState),
            createFrictionJoint(body, gameState.getBackground().getFixture().getBody(), gameState.getWorld()),
            loadAnimations(p1));

        player.getFixture().setUserData(player);

        return player;
    }

//    private static Fixture createAttackFixture(float x, float y, GameState gameState) {
//        BodyDef bodyDef = new BodyDef();
//        bodyDef.type = BodyDef.BodyType.KinematicBody;
//        bodyDef.position.set(x - 1f, y);
//
//        Body body = gameState.getWorld().createBody(bodyDef);
//
//        PolygonShape shape = new PolygonShape();
//        shape.setAsBox(0.5f, 1.5f);
//
//        FixtureDef fixtureDef = new FixtureDef();
//        fixtureDef.shape = shape;
//        fixtureDef.isSensor = true;
//
//        Fixture fixture = body.createFixture(fixtureDef);
//        shape.dispose();
//        return fixture;
//    }

    private static Body createBody(float x, float y, World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);

        return world.createBody(bodyDef);
    }

    private static Fixture createFixture(Body body) {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Player.WIDTH/2, Player.HEIGHT/2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.07f;
        fixtureDef.restitution = 0f;

        Fixture fixture = body.createFixture(fixtureDef);
        shape.dispose();
        return fixture;
    }

    private static FrictionJoint createFrictionJoint(Body body1, Body body2, World world) {
        FrictionJointDef jointDef = new FrictionJointDef();
        jointDef.maxForce = 30f;
        jointDef.maxTorque = 30f;
        jointDef.initialize(body1, body2, new Vector2(0, 0));
        return (FrictionJoint) world.createJoint(jointDef);
    }

    private static PlayerAnimations loadAnimations(boolean p1) {
        if (p1) {
            return new PlayerAnimations(
                AnimationUtil.load(TexturePaths.C1_STANDING_POSE_LEFT, 0.15f, 4, Animation.PlayMode.LOOP),
                AnimationUtil.load(TexturePaths.C1_STANDING_POSE_RIGHT, 0.15f, 4, Animation.PlayMode.LOOP),
                AnimationUtil.load(TexturePaths.C1_RUN_LEFT, 0.10f, 4, Animation.PlayMode.LOOP),
                AnimationUtil.load(TexturePaths.C1_RUN_RIGHT, 0.10f, 4, Animation.PlayMode.LOOP),
                AnimationUtil.load(TexturePaths.C1_ATTACK_LEFT, 0.06f, 6, Animation.PlayMode.LOOP),
                AnimationUtil.load(TexturePaths.C1_ATTACK_RIGHT, 0.06f, 6, Animation.PlayMode.NORMAL)
            );
        }
        return new PlayerAnimations(
            AnimationUtil.load(TexturePaths.C2_STANDING_POSE_LEFT, 0.15f, 4, Animation.PlayMode.LOOP),
            AnimationUtil.load(TexturePaths.C2_STANDING_POSE_RIGHT, 0.15f, 4, Animation.PlayMode.LOOP),
            AnimationUtil.load(TexturePaths.C2_RUN_LEFT, 0.10f, 4, Animation.PlayMode.LOOP),
            AnimationUtil.load(TexturePaths.C2_RUN_RIGHT, 0.10f, 4, Animation.PlayMode.LOOP),
            AnimationUtil.load(TexturePaths.C2_ATTACK_LEFT, 0.06f, 6, Animation.PlayMode.LOOP),
            AnimationUtil.load(TexturePaths.C2_ATTACK_RIGHT, 0.06f, 6, Animation.PlayMode.NORMAL)
        );
    }

}
