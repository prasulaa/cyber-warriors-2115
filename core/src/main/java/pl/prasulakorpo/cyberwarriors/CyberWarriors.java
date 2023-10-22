package pl.prasulakorpo.cyberwarriors;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.java.Log;
import org.java_websocket.client.WebSocketClient;
import pl.prasulakorpo.cyberwarriors.collision.CollisionListener;
import pl.prasulakorpo.cyberwarriors.connection.ConnectionClient;
import pl.prasulakorpo.cyberwarriors.connection.MessageSender;
import pl.prasulakorpo.cyberwarriors.connection.handler.MessageHandlerRepository;
import pl.prasulakorpo.cyberwarriors.drawing.DrawableManager;
import pl.prasulakorpo.cyberwarriors.input.InputHandler;
import pl.prasulakorpo.cyberwarriors.input.MobileControllerUI;
import pl.prasulakorpo.cyberwarriors.model.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

import static pl.prasulakorpo.cyberwarriors.GameProperties.*;

@Log
public class CyberWarriors extends ApplicationAdapter {

	private SpriteBatch batch;
	private OrthographicCamera camera;
    private Viewport viewport;
	private Box2DDebugRenderer debugRenderer;
    private GameState gameState;
    private WebSocketClient client;
    private InputHandler inputHandler;
    private MessageSender messageSender;
    private Stage stage;

    public static float getRatio() {
        return 60f * Gdx.graphics.getDeltaTime();
    }


    @Override
	public void create () {
		HEIGHT = 144;//Gdx.graphics.getHeight(); 9*16 //TODO
		WIDTH = 256;//Gdx.graphics.getWidth(); 16*16 + obramowanie konca swiata

		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, WIDTH, HEIGHT);
        viewport = new FitViewport(WIDTH, HEIGHT, camera);

        gameState = new GameState();

        World world = new World(new Vector2(0, -20), true);
		world.setContactListener(new CollisionListener());

        gameState.setWorld(world);
		debugRenderer = new Box2DDebugRenderer();

        gameState.setDrawableManager(new DrawableManager(new LinkedList<>()));

        try {
            URI uri = new URI("ws://192.168.1.67:8080/servers/" + SERVER_ID);
            client = new ConnectionClient(uri, new ObjectMapper(), new MessageHandlerRepository(gameState));
            client.connect();
            messageSender = new MessageSender(client, new ObjectMapper());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        inputHandler = new InputHandler(gameState);
        stage = MobileControllerUI.createStage(inputHandler);
        Gdx.input.setInputProcessor(stage);

        gameState.setGround(StaticObjectFactory.create(WIDTH/PPM/2, -0.5f, WIDTH/PPM/2, 0.5f, false, gameState.getWorld()));
        gameState.setBackground(StaticObjectFactory.create(WIDTH/PPM/2, HEIGHT/PPM/2, WIDTH/PPM/2, HEIGHT/PPM/2, false, gameState.getWorld()));
        gameState.setLeftWall(StaticObjectFactory.create(-0.5f, HEIGHT/PPM/2, 0.5f, HEIGHT/PPM/2, false, gameState.getWorld()));
        gameState.setRightWall(StaticObjectFactory.create(WIDTH/PPM + 0.5f, HEIGHT/PPM/2, 0.5f, HEIGHT/PPM/2, false, gameState.getWorld()));
        gameState.getPlatforms().add(StaticObjectFactory.create(3.5f, 2.5f, 1.5f, 0.5f, false, world));
        gameState.getPlatforms().add(StaticObjectFactory.create(8f, 2.5f, 1f, 0.5f, false, world));
        gameState.getPlatforms().add(StaticObjectFactory.create(12.5f, 2.5f, 1.5f, 0.5f, false, world));
        gameState.getPlatforms().add(StaticObjectFactory.create(3.5f, 6.5f, 0.5f, 0.5f, false, world));
        gameState.getPlatforms().add(StaticObjectFactory.create(8f, 6.5f, 2f, 0.5f, false, world));
        gameState.getPlatforms().add(StaticObjectFactory.create(12.5f, 6.5f, 0.5f, 0.5f, false, world));
	}


    @Override
	public void render () {
		gameState.updateStateTime(Gdx.graphics.getDeltaTime());

		// GRAPHICS
		camera.update();
		ScreenUtils.clear(0, 0, 0, 1);

        // DRAWABLE
		batch.setProjectionMatrix(camera.combined);

        batch.begin();
        gameState.getDrawableManager().draw(batch, gameState.getStateTime());
		batch.end();

        debugRenderer.render(gameState.getWorld(), camera.combined.scl(PPM));

        // UI
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        // RENDERABLE
        if (gameState.getPlayer() != null) {
            gameState.getPlayer().render();
        }

        // INPUT
        inputHandler.handlePressedKeys();

        // PHYSICS
		doPhysicsStep(Gdx.graphics.getDeltaTime());

        // COMMUNICATION
        messageSender.sendPlayerState(gameState.getPlayer());
	}

	@Override
	public void dispose () {
		batch.dispose();
        client.close();
        stage.dispose();
	}

	private void doPhysicsStep(float deltaTime) {
		float dt = 1/120f;

		while (deltaTime > 0.0) {
			float timeStep = Math.min(deltaTime, dt);
			gameState.getWorld().step(timeStep, 6, 2);
			deltaTime -= timeStep;
		}
	}

}
