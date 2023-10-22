package pl.prasulakorpo.cyberwarriors.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class StaticObject {

    private final Fixture fixture;
    private final float width;
    private final float height;

    public Vector2 getPosition() {
        return fixture.getBody().getPosition();
    }

    public Vector2 getSize() {
        return new Vector2(width, height);
    }

}
