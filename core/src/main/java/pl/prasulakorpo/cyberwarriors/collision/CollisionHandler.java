package pl.prasulakorpo.cyberwarriors.collision;

import com.badlogic.gdx.physics.box2d.Contact;

public interface CollisionHandler {

    void handleBeginContact(Contact contact);
    void handleEndContact(Contact contact);

}
