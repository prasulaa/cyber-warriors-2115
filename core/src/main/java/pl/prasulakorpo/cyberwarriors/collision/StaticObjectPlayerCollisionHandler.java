package pl.prasulakorpo.cyberwarriors.collision;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import pl.prasulakorpo.cyberwarriors.model.Player;
import pl.prasulakorpo.cyberwarriors.model.StaticObject;

public class StaticObjectPlayerCollisionHandler implements CollisionHandler {

    @Override
    public void handleBeginContact(Contact contact) {
        if (contact.getFixtureA().getUserData() instanceof Player){
            handleBeginContact((Player) contact.getFixtureA().getUserData(), (StaticObject) contact.getFixtureB().getUserData());
        } else {
            handleBeginContact((Player) contact.getFixtureB().getUserData(), (StaticObject) contact.getFixtureA().getUserData());
        }
    }

    @Override
    public void handleEndContact(Contact contact) {
        if (contact.getFixtureA().getUserData() instanceof Player){
            handleEndContact((Player) contact.getFixtureA().getUserData(), (StaticObject) contact.getFixtureB().getUserData());
        } else {
            handleEndContact((Player) contact.getFixtureB().getUserData(), (StaticObject) contact.getFixtureA().getUserData());
        }
    }

    private void handleBeginContact(Player player, StaticObject staticObject) {
        Vector2 playerPos = player.getPosition();
        Vector2 staticPos = staticObject.getPosition();
        Vector2 playerSize = player.getSize();
        Vector2 staticSize = staticObject.getSize();

        if (Math.abs(staticPos.y - playerPos.y) <  playerSize.y + staticSize.y) {
            player.setOnWall(true);
        }
    }

    private void handleEndContact(Player player, StaticObject staticObject) {
        Vector2 playerPos = player.getPosition();
        Vector2 staticPos = staticObject.getPosition();
        Vector2 playerSize = player.getSize();
        Vector2 staticSize = staticObject.getPosition();

        if (Math.abs(staticPos.y - playerPos.y) <  playerSize.y + staticSize.y) {
            player.setOnWall(false);
        }
    }

}
