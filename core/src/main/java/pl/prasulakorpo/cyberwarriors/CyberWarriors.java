package pl.prasulakorpo.cyberwarriors;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.java.Log;
import org.java_websocket.client.WebSocketClient;
import pl.prasulakorpo.cyberwarriors.connection.ConnectionClient;
import pl.prasulakorpo.cyberwarriors.connection.MessageSender;
import pl.prasulakorpo.cyberwarriors.connection.handler.MessageHandlerRepository;
import pl.prasulakorpo.cyberwarriors.drawing.DrawableManager;
import pl.prasulakorpo.cyberwarriors.input.InputHandler;
import pl.prasulakorpo.cyberwarriors.input.MobileControllerUI;
import pl.prasulakorpo.cyberwarriors.model.GameProperties;
import pl.prasulakorpo.cyberwarriors.model.GameState;
import pl.prasulakorpo.cyberwarriors.model.Player;
import pl.prasulakorpo.cyberwarriors.model.TexturePaths;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Random;

import static pl.prasulakorpo.cyberwarriors.model.GameProperties.*;
import static pl.prasulakorpo.cyberwarriors.model.TexturePaths.*;

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
        stage = MobileControllerUI.createStage(inputHandler);
        Gdx.input.setInputProcessor(stage);

        createBackgroundFixture();
		createGround();
		createWallLeft();
		createWallRight();
        createPlatform(5f, 2f, 1.5f, 0.5f);
        createPlatform(12f, 4f, 2f, 0.5f);
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

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 2f;
        fixtureDef.friction = 0.07f;
        gameState.setGround(groundBody.createFixture(fixtureDef));

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
