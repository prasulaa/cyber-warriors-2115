package pl.prasulakorpo.cyberwarriors.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import lombok.RequiredArgsConstructor;
import pl.prasulakorpo.cyberwarriors.model.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class InputHandler implements InputProcessor {

    private final Sound attackSound = Gdx.audio.newSound(Gdx.files.internal("assets/atak.wav"));
    private static final float MOVE_IMPULSE = 1f;
    public static final float MAX_VELOCITY = 5f;

    private final GameState gameState;

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.BACKSLASH -> attack(gameState.getPlayer1());
            case Input.Keys.SHIFT_LEFT -> attack(gameState.getPlayer2());
//            case Input.Keys.SPACE -> attack(gameState.getPlayer3());
        }

        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    /**
     * Checks if certain keys are pressed and if so handles them
     */
    public void handlePressedKeys() {
        // PLAYER 1
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            move(-MOVE_IMPULSE, 0, gameState.getPlayer1());
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            move(MOVE_IMPULSE, 0, gameState.getPlayer1());
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            move(0, MOVE_IMPULSE, gameState.getPlayer1());
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            move(0, -MOVE_IMPULSE, gameState.getPlayer1());
        }

        // PLAYER 2
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            move(-MOVE_IMPULSE, 0, gameState.getPlayer2());
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            move(MOVE_IMPULSE, 0, gameState.getPlayer2());
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            move(0, MOVE_IMPULSE, gameState.getPlayer2());
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            move(0, -MOVE_IMPULSE, gameState.getPlayer2());
        }

        // PLAYER 3
//        if (Gdx.input.isKeyPressed(Input.Keys.G)) {
//            move(-MOVE_IMPULSE, 0, gameState.getPlayer3());
//        }
//        if (Gdx.input.isKeyPressed(Input.Keys.J)) {
//            move(MOVE_IMPULSE, 0, gameState.getPlayer3());
//        }
//        if (Gdx.input.isKeyPressed(Input.Keys.Y)) {
//            move(0, MOVE_IMPULSE, gameState.getPlayer3());
//        }
//        if (Gdx.input.isKeyPressed(Input.Keys.H)) {
//            move(0, -MOVE_IMPULSE, gameState.getPlayer3());
//        }
    }

    private void attack(Player player) {
        if (player.isAttackCooldown(gameState.getStateTime())) {
            return;
        }
        List<Skeleton> skeletonsToDelete = new ArrayList<>();

        gameState.getSkeletons().stream()
                .forEach(s -> {if (player.attackOverlaps(s)) skeletonsToDelete.add(s);});

        if (skeletonsToDelete.size() > 0) {
            attackSound.play(1f);
            skeletonsToDelete.forEach(s -> s.hit(gameState));
        }
        player.setLastTimeAttacked(gameState.getStateTime());
    }

    private void move(float impulseX, float impulseY, Player player) {
        if (player == null) {
            return;
        }

        Body body = player.getFixture().getBody();
        Vector2 velocity = body.getLinearVelocity();

        double velX = velocity.x + impulseX;
        double velY = velocity.y + impulseY;
        float vel = (float) Math.sqrt(velX*velX + velY*velY);

        if (vel < MAX_VELOCITY) {
            body.applyLinearImpulse(new Vector2(impulseX, impulseY), body.getPosition(), true);
        }
    }
}
