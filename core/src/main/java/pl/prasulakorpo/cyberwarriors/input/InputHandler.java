package pl.prasulakorpo.cyberwarriors.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.Viewport;
import lombok.RequiredArgsConstructor;
import pl.prasulakorpo.cyberwarriors.model.GameProperties;
import pl.prasulakorpo.cyberwarriors.model.GameState;
import pl.prasulakorpo.cyberwarriors.model.TexturePaths;

import static pl.prasulakorpo.cyberwarriors.model.GameProperties.WIDTH;

@RequiredArgsConstructor
public class InputHandler extends InputListener {

    private static final float IMPULSE = 1.5f;
    private static final float JUMP_IMPULSE = 10f;
    private static final float MAX_VELOCITY = 5f;

    private final GameState gameState;

    private boolean isLeftPressed, isRightPressed, isJumpPressed;

    @Override
    public boolean keyDown(InputEvent event, int keycode) {
        switch (keycode) {
            case Input.Keys.A -> {
                isLeftPressed = true;
                return true;
            }
            case Input.Keys.D -> {
                isRightPressed = true;
                return true;
            }
            case Input.Keys.SPACE -> {
                jump(JUMP_IMPULSE, false);
                isJumpPressed = true;
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean keyUp(InputEvent event, int keycode) {
        switch (keycode) {
            case Input.Keys.A -> {
                isLeftPressed = false;
                return true;
            }
            case Input.Keys.D -> {
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
            jump(0.2f, true);
        }
    }

    private void jump(float impulse, boolean canJumpInAir) {
        Body body = gameState.getPlayer().getFixture().getBody();

        if (Math.abs(body.getLinearVelocity().y) < GameProperties.ERR || canJumpInAir) {
            body.applyLinearImpulse(0, impulse, body.getPosition().x, body.getPosition().y, true);
        }
    }

    private void moveLeft() {
        moveSide(-IMPULSE);
    }

    private void moveRight() {
        moveSide(IMPULSE);
    }

    private void moveSide(float impulse) {
        if (gameState.getPlayer() == null) {
            return;
        }

        Body body = gameState.getPlayer().getFixture().getBody();
        body.applyLinearImpulse(new Vector2(impulse, 0), body.getPosition(), true);

        Vector2 velocity = body.getLinearVelocity();
        if (velocity.x < -MAX_VELOCITY) {
            body.setLinearVelocity(-MAX_VELOCITY, velocity.y);
        }
        if (velocity.x > MAX_VELOCITY) {
            body.setLinearVelocity(MAX_VELOCITY, velocity.y);
        }
    }

}
