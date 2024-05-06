package pl.prasulakorpo.cyberwarriors.collision;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import lombok.RequiredArgsConstructor;
import pl.prasulakorpo.cyberwarriors.model.*;

@RequiredArgsConstructor
public class CollisionListener implements ContactListener {

  private final PlayerSkeletonCollisionHandler playerSkeleton;
  private final SkeletonStaticObjectCollisionHandler skeletonStaticObject;
  private final TowerSkeletonCollisionHandler towerSkeleton;

    public CollisionListener(GameState gameState) {
        this.playerSkeleton = new PlayerSkeletonCollisionHandler();
        this.skeletonStaticObject = new SkeletonStaticObjectCollisionHandler(gameState);
        this.towerSkeleton = new TowerSkeletonCollisionHandler();
    }

    @Override
    public void beginContact(Contact contact) {
        Object userDataA = contact.getFixtureA().getUserData();
        Object userDataB = contact.getFixtureB().getUserData();

        if ((userDataA instanceof Player && userDataB instanceof Skeleton) ||
            userDataA instanceof Skeleton && userDataB instanceof Player) {
            playerSkeleton.handleBeginContact(contact);
        } else if ((userDataA instanceof Skeleton && userDataB instanceof StaticObject) ||
            userDataA instanceof StaticObject && userDataB instanceof Skeleton) {
            skeletonStaticObject.handleBeginContact(contact);
        } else if ((userDataA instanceof Skeleton && userDataB instanceof Tower) ||
            userDataA instanceof Tower && userDataB instanceof Skeleton) {
            towerSkeleton.handleBeginContact(contact);
        }
    }

    @Override
    public void endContact(Contact contact) {
        Object userDataA = contact.getFixtureA().getUserData();
        Object userDataB = contact.getFixtureB().getUserData();

        if ((userDataA instanceof Player && userDataB instanceof Skeleton) ||
            userDataA instanceof Skeleton && userDataB instanceof Player) {
            playerSkeleton.handleEndContact(contact);
        } else if ((userDataA instanceof Skeleton && userDataB instanceof StaticObject) ||
            userDataA instanceof StaticObject && userDataB instanceof Skeleton) {
            skeletonStaticObject.handleEndContact(contact);
        } else if ((userDataA instanceof Skeleton && userDataB instanceof Tower) ||
            userDataA instanceof Tower && userDataB instanceof Skeleton) {
            towerSkeleton.handleEndContact(contact);
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
