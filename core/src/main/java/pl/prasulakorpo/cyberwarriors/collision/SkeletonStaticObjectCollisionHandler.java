package pl.prasulakorpo.cyberwarriors.collision;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import lombok.RequiredArgsConstructor;
import pl.prasulakorpo.cyberwarriors.model.GameState;
import pl.prasulakorpo.cyberwarriors.model.Skeleton;
import pl.prasulakorpo.cyberwarriors.model.StaticObject;

@RequiredArgsConstructor
public class SkeletonStaticObjectCollisionHandler implements CollisionHandler {

    private final GameState gameState;

    @Override
    public void handleBeginContact(Contact contact) {
        Object userDataA = contact.getFixtureA().getUserData();
        Object userDataB = contact.getFixtureB().getUserData();

        if (userDataA instanceof Skeleton && userDataB instanceof StaticObject) {
            handleBeginContact((Skeleton) userDataA, (StaticObject) userDataB);
        } else if (userDataB instanceof Skeleton && userDataA instanceof StaticObject) {
            handleBeginContact((Skeleton) userDataB, (StaticObject) userDataA);
        }
    }

    @Override
    public void handleEndContact(Contact contact) {

    }

    private void handleBeginContact(Skeleton skeleton, StaticObject staticObject) {
        if (!staticObject.getFixture().isSensor()) {
            Gdx.app.postRunnable(() -> {
                gameState.getSkeletons().remove(skeleton);
                gameState.getDrawableManager().delete(skeleton);
                gameState.getWorld().destroyBody(skeleton.getFixture().getBody());
                gameState.incrementSkeletons();
                skeleton.dispose();
            });
        }
    }
}
