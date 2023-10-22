package pl.prasulakorpo.cyberwarriors.collision;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class CollisionListener implements ContactListener {

    private final StaticObjectPlayerCollisionHandler handler = new StaticObjectPlayerCollisionHandler();

    @Override
    public void beginContact(Contact contact) {
        handler.handleBeginContact(contact);
    }

    @Override
    public void endContact(Contact contact) {
        handler.handleEndContact(contact);
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
