package pl.prasulakorpo.cyberwarriors.model;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;

public class SkeletonFactory {

    public static Skeleton create(float x, float y, GameState gameState, boolean s1) {
        Body body = createBody(x, y, gameState.getWorld());
        body.setFixedRotation(true);

        Skeleton skeleton = new Skeleton(
            s1 ? Skeleton.Type.S1 : Skeleton.Type.S2,
            createFixture(body),
            loadSkeletonAnimation(s1),
            s1 ? 1 : 2);

        skeleton.getFixture().setUserData(skeleton);

        return skeleton;
    }

    private static Animation<TextureRegion> loadSkeletonAnimation(boolean s1) {
        if (s1)
            return AnimationUtil.load(TexturePaths.S1_WALK_LEFT, 0.10f, 4, Animation.PlayMode.LOOP);
        return AnimationUtil.load(TexturePaths.S2_WALK_LEFT, 0.10f, 4, Animation.PlayMode.LOOP);
    }

    private static Body createBody(float x, float y, World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);

        return world.createBody(bodyDef);
    }

    private static Fixture createFixture(Body body) {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Skeleton.WIDTH/2, Skeleton.HEIGHT/2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.07f;
        fixtureDef.restitution = 0f;

        Fixture fixture = body.createFixture(fixtureDef);

        shape.dispose();
        return fixture;
    }

}
