package pl.prasulakorpo.cyberwarriors.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import lombok.RequiredArgsConstructor;
import pl.prasulakorpo.cyberwarriors.CyberWarriors;
import pl.prasulakorpo.cyberwarriors.model.GameState;
import pl.prasulakorpo.cyberwarriors.model.Player;

import static pl.prasulakorpo.cyberwarriors.GameProperties.ERR;

@RequiredArgsConstructor
public class InputHandler extends InputListener {

    private static final float MOVE_IMPULSE = 0.6f;
    private static final float MOVE_IN_AIR_RATIO = 0.5f;
    private static final float JUMP_IMPULSE = 9f;
    private static final float SECOND_JUMP_IMPULSE = 5f;
    private static final float JUMP_HIGHER_IMPULSE = 0.4f;
    private static final float JUMP_ON_WALL = 4.5f;
    public static final float MAX_VELOCITY = 5f;
    private static final float MAX_VELOCITY_IN_AIR_RATIO = 1f;
    private static final float DASH_VELOCITY = 10f;
    private static final long JUMP_COOLDOWN_AFTER = 500;
    private static final long DASH_COOLDOWN = 4000;

    private final GameState gameState;

    private boolean isLeftPressed, isRightPressed, isJumpPressed;
    private long lastTimeJump = System.currentTimeMillis();

    @Override
    public boolean keyDown(InputEvent event, int keycode) {
        switch (keycode) {
            case Input.Keys.LEFT -> {
                isLeftPressed = true;
                gameState.getPlayer().setDirectionLeft(true);
                return true;
            }
            case Input.Keys.RIGHT -> {
                isRightPressed = true;
                gameState.getPlayer().setDirectionLeft(false);
                return true;
            }
            case Input.Keys.SPACE -> {
                jumpImpulse();
                lastTimeJump = System.currentTimeMillis();
                isJumpPressed = true;
                return true;
            }
            case Input.Keys.Z -> {
                dash();
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean keyUp(InputEvent event, int keycode) {
        switch (keycode) {
            case Input.Keys.LEFT -> {
                isLeftPressed = false;
                return true;
            }
            case Input.Keys.RIGHT -> {
                isRightPressed = false;
                return true;
            }
            case Input.Keys.SPACE -> {
                isJumpPressed = false;
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if certain keys are pressed and if so handles them
     */
    public void handlePressedKeys() {
        if (isLeftPressed) {
            moveLeft();
        }
        if (isRightPressed) {
            moveRight();
        }
        if (isJumpPressed) {
            jumpHigher();
        }
    }

    private void jumpHigher() {
        Body body = gameState.getPlayer().getFixture().getBody();
        long now = System.currentTimeMillis();

        if (body.getLinearVelocity().y > ERR && now - lastTimeJump < JUMP_COOLDOWN_AFTER) {
            float ratio = CyberWarriors.getRatio() * (1 - (now - lastTimeJump) / (float) JUMP_COOLDOWN_AFTER);
            body.applyLinearImpulse(0, ratio*JUMP_HIGHER_IMPULSE, body.getPosition().x, body.getPosition().y, true);
        }
    }

    private void jumpImpulse() {
        Player player = gameState.getPlayer();
        Body body = player.getFixture().getBody();

        if (gameState.getPlayer().isOnWall()) {
            if (player.isDirectionLeft()) {
                body.setLinearVelocity(JUMP_ON_WALL, JUMP_IMPULSE);
            } else {
                body.setLinearVelocity(-JUMP_ON_WALL, JUMP_IMPULSE);
            }
        } else if (Math.abs(body.getLinearVelocity().y) < ERR) {
            body.setLinearVelocity(body.getLinearVelocity().x, JUMP_IMPULSE);
        } else if (player.isSecondJumpAvailable()) {
            body.setLinearVelocity(body.getLinearVelocity().x, SECOND_JUMP_IMPULSE);
            player.setSecondJumpAvailable(false);
        }
    }

    private void moveLeft() {
        moveSide(CyberWarriors.getRatio() * -MOVE_IMPULSE);
    }

    private void moveRight() {
        moveSide(CyberWarriors.getRatio() * MOVE_IMPULSE);
    }

    private void moveSide(float impulse) {
        if (gameState.getPlayer() == null) {
            return;
        }

        Body body = gameState.getPlayer().getFixture().getBody();
        Vector2 velocity = body.getLinearVelocity();

        float ratio = Math.abs(velocity.y) > ERR ? MOVE_IN_AIR_RATIO : 1f;
        float velocityRatio = Math.abs(velocity.y) > ERR ? MAX_VELOCITY_IN_AIR_RATIO : 1f;
        float velX = velocity.x + ratio*impulse;

        if (Math.abs(velX) < MAX_VELOCITY) {
            body.applyLinearImpulse(new Vector2(ratio*impulse, 0), body.getPosition(), true);
        } else if (velX < -MAX_VELOCITY * velocityRatio && impulse > 0) {
            body.applyLinearImpulse(new Vector2(ratio*impulse, 0), body.getPosition(), true);
        } else if (velX > MAX_VELOCITY * velocityRatio && impulse < 0) {
            body.applyLinearImpulse(new Vector2(ratio * impulse, 0), body.getPosition(), true);
        }
    }

    private void dash() {
        Player player = gameState.getPlayer();
        long now = System.currentTimeMillis();

        if (now - player.getLastDashTime() > DASH_COOLDOWN) {
            player.setLastDashTime(now);
            Body body = player.getFixture().getBody();
            body.setGravityScale(0f);

            body.setLinearVelocity(player.isDirectionLeft() ? -DASH_VELOCITY : DASH_VELOCITY, 0);
        }
    }

}
