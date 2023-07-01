package pl.prasulakorpo.cyberwarriors;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.java.Log;
import org.java_websocket.client.WebSocketClient;
import pl.prasulakorpo.cyberwarriors.connection.ConnectionClient;
import pl.prasulakorpo.cyberwarriors.connection.MessageSender;
import pl.prasulakorpo.cyberwarriors.connection.handler.MessageHandlerRepository;
import pl.prasulakorpo.cyberwarriors.drawing.DrawableManager;
import pl.prasulakorpo.cyberwarriors.input.InputHandler;
import pl.prasulakorpo.cyberwarriors.model.GameProperties;
import pl.prasulakorpo.cyberwarriors.model.GameState;
import pl.prasulakorpo.cyberwarriors.model.Player;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Random;

import static pl.prasulakorpo.cyberwarriors.model.GameProperties.*;

@Log
public class CyberWarriors extends ApplicationAdapter {

	private SpriteBatch batch;
	private OrthographicCamera camera;
	private Box2DDebugRenderer debugRenderer;
    private GameState gameState;
    private WebSocketClient client;
    private InputHandler inputHandler;
    private MessageSender messageSender;


    @Override
	public void create () {
		HEIGHT = 240;//Gdx.graphics.getHeight();
		WIDTH = 400;//Gdx.graphics.getWidth();

		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, WIDTH, HEIGHT);

        gameState = new GameState();

		gameState.setWorld(new World(new Vector2(0, -20), true));
		debugRenderer = new Box2DDebugRenderer();

        gameState.setDrawableManager(new DrawableManager());

        try {
            URI uri = new URI("ws://192.168.1.67:8080/servers/" + SERVER_ID);
            client = new ConnectionClient(uri, new ObjectMapper(), new MessageHandlerRepository(gameState));
            client.connect();
            messageSender = new MessageSender(client, new ObjectMapper());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        inputHandler = new InputHandler(gameState);
        Gdx.input.setInputProcessor(inputHandler);

        createBackgroundFixture();
		createGround();
		createWallLeft();
		createWallRight();
//        createPlatform(10f, 3f, 2f, 0.5f);
//        createPlatform(20f, 5f, 2f, 0.5f);
//        createPlatform(30f, 7f, 2f, 0.5f);
//        createPlatform(37f, 8f, 1f, 0.5f);
//        createPlatform(49f, 11f, 2f, 0.5f);
	}

	@Override
	public void render () {
		gameState.updateStateTime(Gdx.graphics.getDeltaTime());

		// GRAPHICS
		camera.update();
		ScreenUtils.clear(0, 0, 0, 1);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
        gameState.getDrawableManager().draw(batch, gameState.getStateTime());
		batch.end();
//		debugRenderer.render(gameState.getWorld(), camera.combined.scl(PPM));

        inputHandler.handlePressedKeys();

		doPhysicsStep(Gdx.graphics.getDeltaTime());

        messageSender.sendPlayerState(gameState.getPlayer());
	}

	@Override
	public void dispose () {
		batch.dispose();
        client.close();
	}

	private void doPhysicsStep(float deltaTime) {
		float dt = 1/120f;

		while (deltaTime > 0.0) {
			float timeStep = Math.min(deltaTime, dt);
			gameState.getWorld().step(timeStep, 6, 2);
			deltaTime -= timeStep;
		}
	}

	private void createGround() {
        createPlatform(WIDTH/PPM/2, -0.5f, WIDTH/PPM/2, 0.5f);
	}

	private void createWallLeft() {
		BodyDef groundBodyDef = new BodyDef();
		groundBodyDef.position.set(-0.5f, WIDTH/PPM/2);
		groundBodyDef.type = BodyDef.BodyType.StaticBody;

		Body groundBody = gameState.getWorld().createBody(groundBodyDef);

		PolygonShape groundBox = new PolygonShape();
		groundBox.setAsBox(0.5f, WIDTH);
		groundBody.createFixture(groundBox, 0.5f);

		groundBox.dispose();
	}

	private void createWallRight() {
		BodyDef groundBodyDef = new BodyDef();
		groundBodyDef.position.set(WIDTH/PPM + 0.6f, WIDTH/PPM/2);
		groundBodyDef.type = BodyDef.BodyType.StaticBody;

		Body groundBody = gameState.getWorld().createBody(groundBodyDef);

		PolygonShape groundBox = new PolygonShape();
		groundBox.setAsBox(0.5f, WIDTH);
		groundBody.createFixture(groundBox, 0.0f);

		groundBox.dispose();
	}

    private void createPlatform(float posX, float posY, float width, float height) {
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.position.set(posX, posY);
        groundBodyDef.type = BodyDef.BodyType.StaticBody;

        Body groundBody = gameState.getWorld().createBody(groundBodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width, height);
        gameState.setGround(groundBody.createFixture(shape, 2f));

        shape.dispose();
    }

    private void createBackgroundFixture() {
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.position.set(WIDTH/2/PPM, HEIGHT/2/PPM);
        groundBodyDef.type = BodyDef.BodyType.StaticBody;

        Body groundBody = gameState.getWorld().createBody(groundBodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(WIDTH/2/PPM, HEIGHT/2/PPM);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.0f;
        fixtureDef.isSensor = true;

        gameState.setBackground(groundBody.createFixture(fixtureDef));

        shape.dispose();
    }

}
