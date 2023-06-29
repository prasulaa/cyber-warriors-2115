package pl.prasulakorpo.cyberwarriors.connection.handler;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.joints.FrictionJoint;
import com.badlogic.gdx.physics.box2d.joints.FrictionJointDef;
import org.java_websocket.client.WebSocketClient;
import pl.prasulakorpo.cyberwarriors.connection.message.GeneralMsg;
import pl.prasulakorpo.cyberwarriors.connection.message.PlayerDTO;
import pl.prasulakorpo.cyberwarriors.model.GameState;
import pl.prasulakorpo.cyberwarriors.model.Player;

public class PlayerDTOHandler extends MessageHandler {

    public PlayerDTOHandler(GameState gameState) {
        super(gameState);
    }

    @Override
    public void handle(GeneralMsg msg, WebSocketClient client) {
        PlayerDTO playerMsg = (PlayerDTO) msg;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(playerMsg.getX(), playerMsg.getY());

        Body body = gameState.getWorld().createBody(bodyDef);
        body.setFixedRotation(true);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.5f, 1f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;

        FrictionJointDef jointDef = new FrictionJointDef();
        jointDef.maxForce = 1f;
        jointDef.maxTorque = 1f;
        jointDef.initialize(body, gameState.getBackground().getBody(), new Vector2(0, 0));
        FrictionJoint frictionJoint = (FrictionJoint) gameState.getWorld().createJoint(jointDef);

        gameState.setPlayer(new Player(body.createFixture(fixtureDef), frictionJoint));
        shape.dispose();
    }

}
