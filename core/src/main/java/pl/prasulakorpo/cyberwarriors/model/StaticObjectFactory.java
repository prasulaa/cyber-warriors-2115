package pl.prasulakorpo.cyberwarriors.model;

import com.badlogic.gdx.physics.box2d.*;

public class StaticObjectFactory {

    public static StaticObject create(float posX, float posY, float width, float height, boolean isSensor, World world) {
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.position.set(posX, posY);
        groundBodyDef.type = BodyDef.BodyType.StaticBody;

        Body groundBody = world.createBody(groundBodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width, height);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 2f;
        fixtureDef.friction = 0.07f;
        Fixture fixture = groundBody.createFixture(fixtureDef);

        shape.dispose();

        StaticObject staticObject = new StaticObject(fixture, width, height);
        fixture.setUserData(staticObject);

        return staticObject;
    }

}
