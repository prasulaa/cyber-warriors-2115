package pl.prasulakorpo.cyberwarriors.collision;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import pl.prasulakorpo.cyberwarriors.model.Skeleton;
import pl.prasulakorpo.cyberwarriors.model.Tower;

public class TowerSkeletonCollisionHandler implements CollisionHandler {
    @Override
    public void handleBeginContact(Contact contact) {
        Object userDataA = contact.getFixtureA().getUserData();
        Object userDataB = contact.getFixtureB().getUserData();

        if (userDataA instanceof Skeleton && userDataB instanceof Tower) {
            handleBeginContact((Skeleton) userDataA, (Tower) userDataB);
        } else if (userDataB instanceof Skeleton && userDataA instanceof Tower) {
            handleBeginContact((Skeleton) userDataB, (Tower) userDataA);
        }
    }

    @Override
    public void handleEndContact(Contact contact) {
        Object userDataA = contact.getFixtureA().getUserData();
        Object userDataB = contact.getFixtureB().getUserData();

        if (userDataA instanceof Skeleton && userDataB instanceof Tower) {
            handleEndContact((Skeleton) userDataA, (Tower) userDataB);
        } else if (userDataB instanceof Skeleton && userDataA instanceof Tower) {
            handleEndContact((Skeleton) userDataB, (Tower) userDataA);
        }
    }

    private void handleBeginContact(Skeleton skeleton, Tower tower) {
        Gdx.app.postRunnable(() -> tower.getSkeletons().add(skeleton));
    }

    private void handleEndContact(Skeleton skeleton, Tower tower) {
        Gdx.app.postRunnable(() -> tower.getSkeletons().remove(skeleton));
    }
}
