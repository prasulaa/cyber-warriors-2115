package pl.prasulakorpo.cyberwarriors.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import lombok.AllArgsConstructor;
import pl.prasulakorpo.cyberwarriors.model.GameProperties;
import pl.prasulakorpo.cyberwarriors.model.GameState;
import pl.prasulakorpo.cyberwarriors.model.Player;

@AllArgsConstructor
public class InputHandler extends InputAdapter {

    private static final float IMPULSE = 1.5f;
    private static final float MAX_VELOCITY = 5f;

    private final GameState gameState;



    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.SPACE) {
            jump(20f, false);
            return true;
        }

        return false;
    }

    /**
     * Checks if certain keys are pressed and if so handles them
     */
    public void handlePressedKeys() {
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            moveLeft();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            moveRight();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            jump(0.3f, true);
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
