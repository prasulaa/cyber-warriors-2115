package pl.prasulakorpo.cyberwarriors.drawing;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public interface Drawable {

    /**
     *
     * @return middle of the object
     */
    Vector2 getPosition();
    TextureRegion getTextureRegion(float stateTime);

}
