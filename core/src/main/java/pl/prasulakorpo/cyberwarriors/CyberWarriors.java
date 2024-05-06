package pl.prasulakorpo.cyberwarriors;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import lombok.extern.java.Log;
import pl.prasulakorpo.cyberwarriors.collision.CollisionListener;
import pl.prasulakorpo.cyberwarriors.drawing.Drawable;
import pl.prasulakorpo.cyberwarriors.drawing.DrawableManager;
import pl.prasulakorpo.cyberwarriors.input.InputHandler;
import pl.prasulakorpo.cyberwarriors.model.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static pl.prasulakorpo.cyberwarriors.GameProperties.*;

@Log
public class CyberWarriors extends ApplicationAdapter {

	private SpriteBatch batch;
	private OrthographicCamera camera;
    private Viewport viewport;
	private Box2DDebugRenderer debugRenderer;
    private GameState gameState;
    private InputHandler inputHandler;
    private Random random = new Random();

    public static float getRatio() {
        return 60f * Gdx.graphics.getDeltaTime();
    }

    @Override
	public void create () {
        batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, WIDTH, HEIGHT);
        viewport = new FitViewport(WIDTH, HEIGHT, camera);

        gameState = new GameState();

        World world = new World(new Vector2(0, 0), true);
		world.setContactListener(new CollisionListener(gameState));

        gameState.setWorld(world);
		debugRenderer = new Box2DDebugRenderer();

        gameState.setDrawableManager(new DrawableManager(new LinkedList<>()));

        inputHandler = new InputHandler(gameState);
        Gdx.input.setInputProcessor(inputHandler);

        // CREATE BORDERS AND BACKGROUND WITH FRICTION
        gameState.getPlatforms().addAll(List.of(
            StaticObjectFactory.create(WIDTH/PPM/2, -0.5f, WIDTH/PPM/2, 0.5f, false, gameState.getWorld()),
            StaticObjectFactory.create(WIDTH/PPM/2, HEIGHT/PPM - 3.5f, WIDTH/PPM/2, 0.5f, false, gameState.getWorld()),
            StaticObjectFactory.create(16.5f, HEIGHT/PPM - 4.5f, 6, 0.5f, false, gameState.getWorld()),
            StaticObjectFactory.create(19.5f, HEIGHT/PPM - 5.5f, 3, 0.5f, false, gameState.getWorld()),
            StaticObjectFactory.create(-0.5f, HEIGHT/PPM/2, 0.5f, HEIGHT/PPM/2, false, gameState.getWorld()),
            StaticObjectFactory.create(WIDTH/PPM + 0.5f, HEIGHT/PPM/2, 0.5f, HEIGHT/PPM/2, false, gameState.getWorld())));
        gameState.setBackground(StaticObjectFactory.create(WIDTH/PPM/2, HEIGHT/PPM/2, WIDTH/PPM/2, HEIGHT/PPM/2, true, gameState.getWorld()));
        gameState.getDrawableManager().add(new Background());

        // CREATE PLAYERS
        if (PLAYERS > 0) {
            var player1 = PlayerFactory.create("1", 5, 5, true, gameState);
            gameState.setPlayer1(player1);
            gameState.getDrawableManager().add(player1);
        }
        if (PLAYERS > 1) {
            var player2 = PlayerFactory.create("2", 5, 10, false, gameState);
            gameState.setPlayer2(player2);
            gameState.getDrawableManager().add(player2);
        }

        Tower t1 = TowerFactory.create(new Vector2(6f, 2f), gameState.getStateTime(), gameState.getWorld());
        gameState.getTowers().add(t1);
        gameState.getDrawableManager().add(1, t1);

        Tower t2 = TowerFactory.create(new Vector2(14f, 5.5f), gameState.getStateTime(), gameState.getWorld());
        gameState.getTowers().add(t2);
        gameState.getDrawableManager().add(1, t2);

        Effect birds = EffectFactory.birds(new Vector2(WIDTH/PPM/2, HEIGHT/PPM/2), gameState.getStateTime());
        gameState.getEffects().add(birds);
        gameState.getDrawableManager().add(1, birds);

        gameState.setMusic(Gdx.audio.newSound(Gdx.files.internal("assets/Project_4.mp3")));
        gameState.getMusic().play(0.5f);
        gameState.getMusic().loop();
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

//        debugRenderer.render(gameState.getWorld(), camera.combined.scl(PPM));

        // INPUT
        inputHandler.handlePressedKeys();

        // RENDERABLE
        gameState.getSkeletons().forEach(Skeleton::render);
        gameState.getEffects().forEach(e -> e.render(gameState));
        gameState.getTowers().forEach(t -> t.render(gameState));

        // PHYSICS
		doPhysicsStep(Gdx.graphics.getDeltaTime());

        // SPAWN SKELETONS
        float skeletonSpawnRatio = 0.02f;
        float skeleton2SpawnRatio = 0.15f;
        if (random.nextFloat() < skeletonSpawnRatio) {
            Skeleton s = SkeletonFactory.create(
                WIDTH/PPM - 1.5f,
                0.5f + random.nextInt(7),
                gameState,
                random.nextFloat() > skeleton2SpawnRatio);
            gameState.getSkeletons().add(s);
            gameState.getDrawableManager().add(s);
        }
	}

	@Override
	public void dispose () {
		batch.dispose();
        gameState.getMusic().dispose();
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
