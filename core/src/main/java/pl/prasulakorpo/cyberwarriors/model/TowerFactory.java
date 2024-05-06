package pl.prasulakorpo.cyberwarriors.model;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import static pl.prasulakorpo.cyberwarriors.model.TexturePaths.T1_ATTACK;
import static pl.prasulakorpo.cyberwarriors.model.TexturePaths.T1_STAND;

public class TowerFactory {
    public static Tower create(Vector2 position, float stateTime, World world) {
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.position.set(position);
        groundBodyDef.type = BodyDef.BodyType.StaticBody;

        Body groundBody = world.createBody(groundBodyDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(1f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        Fixture fixture = groundBody.createFixture(fixtureDef);
        fixture.setSensor(true);

        shape.dispose();

        var tower = new Tower(
            fixture,
            AnimationUtil.load(T1_ATTACK, 0.1f, 8, Animation.PlayMode.LOOP),
            AnimationUtil.load(T1_STAND, 0.1f, 1, Animation.PlayMode.LOOP),
            5f,
            stateTime
        );

        fixture.setUserData(tower);

        return tower;
    }
}
