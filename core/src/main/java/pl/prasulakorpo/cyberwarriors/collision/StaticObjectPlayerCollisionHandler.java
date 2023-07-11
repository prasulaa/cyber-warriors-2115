package pl.prasulakorpo.cyberwarriors.collision;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import pl.prasulakorpo.cyberwarriors.model.Player;
import pl.prasulakorpo.cyberwarriors.model.StaticObject;

public class StaticObjectPlayerCollisionHandler implements CollisionHandler {

    @Override
    public void handleBeginContact(Contact contact) {
        if (contact.getFixtureA().getUserData() instanceof Player && contact.getFixtureB().getUserData() instanceof StaticObject) {
            handleBeginContact((Player) contact.getFixtureA().getUserData(), (StaticObject) contact.getFixtureB().getUserData());
        } else if (contact.getFixtureA().getUserData() instanceof StaticObject && contact.getFixtureB().getUserData() instanceof Player){
            handleBeginContact((Player) contact.getFixtureB().getUserData(), (StaticObject) contact.getFixtureA().getUserData());
        }
    }

    @Override
    public void handleEndContact(Contact contact) {
        if (contact.getFixtureA().getUserData() instanceof Player && contact.getFixtureB().getUserData() instanceof StaticObject) {
            handleEndContact((Player) contact.getFixtureA().getUserData(), (StaticObject) contact.getFixtureB().getUserData());
        } else if (contact.getFixtureA().getUserData() instanceof StaticObject && contact.getFixtureB().getUserData() instanceof Player){
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
        Vector2 staticSize = staticObject.getSize();

        player.setOnWall(false);

         if (staticPos.y - playerPos.y < staticSize.y + playerSize.y) { // ON WALL OR TOP
             System.out.println("ON WALL OR TOP");
             player.setSecondJumpAvailable(true);
        }
    }

}
