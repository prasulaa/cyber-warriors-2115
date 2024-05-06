package pl.prasulakorpo.cyberwarriors.drawing;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static pl.prasulakorpo.cyberwarriors.GameProperties.PPM;

@AllArgsConstructor
public class DrawableManager {

    private final List<Drawable> drawables;

    public void add(Drawable drawable) {
        drawables.add(drawable);
    }

    public void add(int i, Drawable drawable) {
        drawables.add(i, drawable);
    }

    public void addOnEnd(Drawable drawable) {
        drawables.add(drawables.size() - 1, drawable);
    }

    public void delete(Drawable drawable) {
        drawables.remove(drawable);
    }

    public void draw(SpriteBatch batch, float stateTime) {
        drawables.forEach(d -> {
            Vector2 pos = d.getPosition();
            TextureRegion textureRegion = d.getTextureRegion(stateTime);

            batch.draw(
                textureRegion,
                pos.x*PPM - textureRegion.getRegionWidth()/2f,
                pos.y*PPM - textureRegion.getRegionHeight()/2f
            );
        });
    }

}
