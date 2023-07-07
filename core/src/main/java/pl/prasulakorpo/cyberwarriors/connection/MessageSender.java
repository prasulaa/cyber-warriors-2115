package pl.prasulakorpo.cyberwarriors.connection;

import com.badlogic.gdx.math.Vector2;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.java_websocket.client.WebSocketClient;
import pl.prasulakorpo.cyberwarriors.connection.message.PlayerStateDTO;
import pl.prasulakorpo.cyberwarriors.model.Player;

import static pl.prasulakorpo.cyberwarriors.model.GameProperties.SERVER_TICKRATE;

@RequiredArgsConstructor
public class MessageSender {
    private final WebSocketClient client;
    private final ObjectMapper mapper;
    private long lastTimeSend = System.currentTimeMillis();

    public void sendPlayerState(Player player) {
        if (player == null || System.currentTimeMillis() - lastTimeSend < 1000/SERVER_TICKRATE) {
            return;
        }

        Vector2 pos = player.getPosition();
        PlayerStateDTO playerStateDTO = new PlayerStateDTO(pos.x, pos.y);

        try {
            client.send(mapper.writeValueAsString(playerStateDTO));
            lastTimeSend = System.currentTimeMillis();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
