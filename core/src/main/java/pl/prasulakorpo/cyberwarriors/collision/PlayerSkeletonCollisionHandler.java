package pl.prasulakorpo.cyberwarriors.collision;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import pl.prasulakorpo.cyberwarriors.model.Player;
import pl.prasulakorpo.cyberwarriors.model.Skeleton;

public class PlayerSkeletonCollisionHandler implements CollisionHandler {

  private final Sound meowSound = Gdx.audio.newSound(Gdx.files.internal("assets/meow.wav"));

    @Override
    public void handleBeginContact(Contact contact) {
        Object userDataA = contact.getFixtureA().getUserData();
        Object userDataB = contact.getFixtureB().getUserData();

        if (userDataA instanceof Player && userDataB instanceof Skeleton) {
            handleBeginContact((Player) userDataA, (Skeleton) userDataB);
        } else if (userDataA instanceof Skeleton && userDataB instanceof Player) {
            handleBeginContact((Player) userDataB, (Skeleton) userDataA);
        }
    }

    @Override
    public void handleEndContact(Contact contact) {

    }

    private void handleBeginContact(Player player, Skeleton skeleton) {
        Vector2 playerPos = player.getPosition();
        Vector2 skeletonPos = skeleton.getPosition();

        Vector2 direction = new Vector2(playerPos.x - skeletonPos.x, playerPos.y - skeletonPos.y)
            .nor()
            .scl(skeleton.getType() == Skeleton.Type.S1 ? 9f : 15f);

        player.getFixture().getBody().setLinearVelocity(direction);

        meowSound.play(1f);
    }
}
