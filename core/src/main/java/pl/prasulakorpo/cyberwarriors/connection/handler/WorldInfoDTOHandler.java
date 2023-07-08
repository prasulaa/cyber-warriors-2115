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

public class WorldInfoDTOHandler extends MessageHandler {
    private static final float IMPULSE = 20f;
    private static final float FLOAT_ERR = 4*1e-2f;
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

    public static void main(String[] args) {
        solveEquations();
    }

    public static void solveEquations() {
        double[] x = {-3, 2, 10}; // Wartości x
        double[] y = {-1, 3, 8}; // Wartości y

        // Tworzenie macierzy A na podstawie wartości x^2, x i stałej
        double[][] coefficients = new double[x.length][3];
        for (int i = 0; i < x.length; i++) {
            coefficients[i][0] = x[i] * x[i];
            coefficients[i][1] = x[i];
            coefficients[i][2] = 1;
        }

        RealMatrix A = new Array2DRowRealMatrix(coefficients, false);

        // Tworzenie wektora b na podstawie wartości y
        RealMatrix B = new Array2DRowRealMatrix(y);

        // Rozwiązanie układu równań
        DecompositionSolver solver = new LUDecomposition(A).getSolver();
        RealMatrix solution = solver.solve(B);

        // Otrzymane wartości a, b, c
        double a = solution.getEntry(0, 0);
        double b = solution.getEntry(1, 0);
        double c = solution.getEntry(2, 0);

        // Wyświetlanie wyników
        System.out.println("a = " + a);
        System.out.println("b = " + b);
        System.out.println("c = " + c);
    }














}
