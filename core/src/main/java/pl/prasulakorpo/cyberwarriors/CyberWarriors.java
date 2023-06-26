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
import pl.prasulakorpo.cyberwarriors.connection.handler.MessageHandlerRepository;
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



    @Override
	public void create () {
		HEIGHT = Gdx.graphics.getHeight();
		WIDTH = Gdx.graphics.getWidth();

		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, WIDTH, HEIGHT);

        gameState = new GameState();

		gameState.setWorld(new World(new Vector2(0, -10), true));
		debugRenderer = new Box2DDebugRenderer();

        try {
            URI uri = new URI("ws://192.168.1.67:8080/servers/" + SERVER_ID);
            client = new ConnectionClient(uri, new ObjectMapper(), new MessageHandlerRepository(gameState));
            client.connect();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

		createGround();
		createWallLeft();
		createWallRight();
	}

	@Override
	public void render () {
		gameState.updateStateTime(Gdx.graphics.getDeltaTime());

		// GRAPHICS
		camera.update();
		ScreenUtils.clear(0, 0, 0, 1);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		batch.end();
		debugRenderer.render(gameState.getWorld(), camera.combined.scl(PPM));

		doPhysicsStep(Gdx.graphics.getDeltaTime());
	}

	@Override
	public void dispose () {
		batch.dispose();
        client.close();
	}

	private void doPhysicsStep(float deltaTime) {
		float dt = 1/2000f;

		while (deltaTime > 0.0) {
			float timeStep = Math.min(deltaTime, dt);
			gameState.getWorld().step(timeStep, 6, 2);
			deltaTime -= timeStep;
		}
	}

	private void createGround() {
		BodyDef groundBodyDef = new BodyDef();
		groundBodyDef.position.set(WIDTH/PPM/2, -0.5f);
		groundBodyDef.type = BodyDef.BodyType.StaticBody;

		Body groundBody = gameState.getWorld().createBody(groundBodyDef);

		PolygonShape groundBox = new PolygonShape();
		groundBox.setAsBox(WIDTH/PPM/2, 0.5f);
		gameState.setGround(groundBody.createFixture(groundBox, 0.0f));

		groundBox.dispose();
	}

	private void createWallLeft() {
		BodyDef groundBodyDef = new BodyDef();
		groundBodyDef.position.set(-0.5f, WIDTH/PPM/2);
		groundBodyDef.type = BodyDef.BodyType.StaticBody;

		Body groundBody = gameState.getWorld().createBody(groundBodyDef);

		PolygonShape groundBox = new PolygonShape();
		groundBox.setAsBox(0.5f, WIDTH);
		groundBody.createFixture(groundBox, 0.0f);

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


}
