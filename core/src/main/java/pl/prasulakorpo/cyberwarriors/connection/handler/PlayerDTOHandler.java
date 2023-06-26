package pl.prasulakorpo.cyberwarriors.connection.handler;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
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
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.5f, 1f);

        gameState.setPlayer(new Player(body.createFixture(shape, 0.5f)));
        shape.dispose();
    }

}
