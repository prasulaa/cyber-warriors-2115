package pl.prasulakorpo.cyberwarriors.connection.handler;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import org.java_websocket.client.WebSocketClient;
import pl.prasulakorpo.cyberwarriors.connection.message.GeneralMsg;
import pl.prasulakorpo.cyberwarriors.connection.message.PlayerDTO;
import pl.prasulakorpo.cyberwarriors.connection.message.WorldInfoDTO;
import pl.prasulakorpo.cyberwarriors.model.GameState;
import pl.prasulakorpo.cyberwarriors.model.Player;
import pl.prasulakorpo.cyberwarriors.model.PlayerFactory;

import java.util.Map;

public class WorldInfoDTOHandler extends MessageHandler {
    private static final float IMPULSE = 20f;

    public WorldInfoDTOHandler(GameState gameState) {
        super(gameState);
    }

    @Override
    public void handle(GeneralMsg msg, WebSocketClient client) {
        WorldInfoDTO worldInfo = (WorldInfoDTO) msg;
        Map<String, Player> players = gameState.getPlayers();

        worldInfo.getPlayers().forEach(playerDTO -> {
            Player p;
            if ((p = players.get(playerDTO.getId())) != null) {
                updatePlayer(p, playerDTO);
            } else if (playerDTO.getId().equals(gameState.getPlayer().getId())) {
                // update main player
            } else {
                Player player = PlayerFactory.create(playerDTO.getId(), playerDTO.getX(), playerDTO.getY(), gameState);
                players.put(player.getId(), player);
            }
        });
    }

    private void updatePlayer(Player player, PlayerDTO playerDTO) {
        Vector2 playerPos = player.getPosition();
        float movX = playerDTO.getX() - playerPos.x;
        float movY = playerDTO.getY() - playerPos.y;

        player.getFixture().getBody().setLinearVelocity(movX * IMPULSE, movY * IMPULSE);
    }

}
