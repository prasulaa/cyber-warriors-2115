package pl.prasulakorpo.cyberwarriors.connection.handler;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.java_websocket.client.WebSocketClient;
import pl.prasulakorpo.cyberwarriors.connection.message.GeneralMsg;
import pl.prasulakorpo.cyberwarriors.connection.message.PlayerDTO;
import pl.prasulakorpo.cyberwarriors.connection.message.WorldInfoDTO;
import pl.prasulakorpo.cyberwarriors.model.GameState;
import pl.prasulakorpo.cyberwarriors.model.Player;
import pl.prasulakorpo.cyberwarriors.model.PlayerFactory;

import java.util.Map;
import java.util.stream.Collectors;

import static pl.prasulakorpo.cyberwarriors.GameProperties.SERVER_TICKRATE;

public class WorldInfoDTOHandler extends MessageHandler {
    private static final float IMPULSE = SERVER_TICKRATE;
    private static final float FLOAT_ERR = 0.00313f;
    private static final float TELEPORT_ERR = 1.5f;

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
                initPlayer(playerDTO, players);
            }
        });

        players.entrySet().stream()
            .filter(e -> worldInfo.getPlayers().stream().noneMatch(p -> p.getId().equals(e.getValue().getId())))
            .collect(Collectors.toList())
            .forEach(e -> {
                Player p = e.getValue();
                players.remove(e.getKey());
                gameState.getWorld().destroyJoint(p.getFrictionJoint());
                gameState.getWorld().destroyBody(p.getFixture().getBody());
                gameState.getDrawableManager().delete(p);
                p.dispose();
            });
    }

    private void initPlayer(PlayerDTO playerDTO, Map<String, Player> players) {
        Player player = PlayerFactory.create(playerDTO.getId(), playerDTO.getX(), playerDTO.getY(), gameState);
        players.put(player.getId(), player);
        gameState.getDrawableManager().add(player);
    }

    private void updatePlayer(Player player, PlayerDTO playerDTO) {
        Vector2 playerPos = player.getPosition();

        float movX = playerDTO.getX() - playerPos.x;
        float movY = playerDTO.getY() - playerPos.y;
        double r = Math.sqrt(movX*movX + movY*movY);

        if (r > TELEPORT_ERR) {
            Body body = player.getFixture().getBody();
            body.setTransform(playerDTO.getX(), playerDTO.getY(), 0f);
            body.setLinearVelocity(0, 0);
        } else {
            player.getFixture().getBody().setLinearVelocity(
                Math.abs(movX) > FLOAT_ERR ? movX * IMPULSE : 0f,
                Math.abs(movY) > FLOAT_ERR ? movY * IMPULSE : 0f
            );
        }
    }

}
