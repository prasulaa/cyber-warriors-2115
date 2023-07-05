package pl.prasulakorpo.cyberwarriors.connection.handler;

import lombok.AllArgsConstructor;
import org.java_websocket.client.WebSocketClient;
import pl.prasulakorpo.cyberwarriors.connection.message.GeneralMsg;
import pl.prasulakorpo.cyberwarriors.drawing.DrawableManager;
import pl.prasulakorpo.cyberwarriors.model.GameState;

@AllArgsConstructor
public abstract class MessageHandler {

    protected final GameState gameState;

    public abstract void handle(GeneralMsg msg, WebSocketClient client);

}
