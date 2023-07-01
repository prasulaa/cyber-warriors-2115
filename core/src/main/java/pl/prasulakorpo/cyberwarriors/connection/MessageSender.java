package pl.prasulakorpo.cyberwarriors.connection;

import com.badlogic.gdx.math.Vector2;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.java_websocket.client.WebSocketClient;
import pl.prasulakorpo.cyberwarriors.connection.message.PlayerStateDTO;
import pl.prasulakorpo.cyberwarriors.model.Player;

@AllArgsConstructor
public class MessageSender {
    private final WebSocketClient client;
    private final ObjectMapper mapper;

    public void sendPlayerState(Player player) {
        if (player == null) {
            return;
        }

        Vector2 pos = player.getPosition();
        PlayerStateDTO playerStateDTO = new PlayerStateDTO(pos.x, pos.y);

        try {
            client.send(mapper.writeValueAsString(playerStateDTO));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
