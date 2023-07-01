package pl.prasulakorpo.cyberwarriors.connection.handler;

import org.java_websocket.client.WebSocketClient;
import pl.prasulakorpo.cyberwarriors.connection.message.GeneralMsg;
import pl.prasulakorpo.cyberwarriors.connection.message.PlayerDTO;
import pl.prasulakorpo.cyberwarriors.drawing.DrawableManager;
import pl.prasulakorpo.cyberwarriors.model.GameState;
import pl.prasulakorpo.cyberwarriors.model.Player;
import pl.prasulakorpo.cyberwarriors.model.PlayerFactory;

public class PlayerDTOHandler extends MessageHandler {

    public PlayerDTOHandler(GameState gameState) {
        super(gameState);
    }

    @Override
    public void handle(GeneralMsg msg, WebSocketClient client) {
        PlayerDTO playerMsg = (PlayerDTO) msg;

        Player player = PlayerFactory.create(playerMsg.getId(), playerMsg.getX(), playerMsg.getY(), gameState);

        gameState.setPlayer(player);
        gameState.getDrawableManager().add(player);
    }

}
