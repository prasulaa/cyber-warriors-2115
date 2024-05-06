package pl.prasulakorpo.cyberwarriors.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pl.prasulakorpo.cyberwarriors.drawing.Drawable;

import java.util.LinkedList;
import java.util.List;

@RequiredArgsConstructor
public class Tower implements Drawable {

    private final Fixture fixture;
    private final Animation<TextureRegion> animation;
    private final Animation<TextureRegion> stillAnimation;
    private final float stillAnimationTime;
    private final float creationTime;
    @Getter
    private boolean isActive;
    @Getter
    private List<Skeleton> skeletons = new LinkedList<>();
    private final Sound sound = Gdx.audio.newSound(Gdx.files.internal("assets/tower.wav"));

    @Override
    public Vector2 getPosition() {
        return fixture.getBody().getPosition();
    }

    @Override
    public TextureRegion getTextureRegion(float stateTime) {
        float time = (stateTime - creationTime);
        float x = time / (animation.getAnimationDuration() + stillAnimationTime);

        if (x - Math.floor(x) < stillAnimationTime / (animation.getAnimationDuration() + stillAnimationTime)) {
            isActive = false;
            return stillAnimation.getKeyFrame(stateTime - creationTime);
        }
        if (!isActive) {
            sound.play(1f);
        }

        isActive = true;
        return animation.getKeyFrame(time - (float) (Math.floor(x) * (animation.getAnimationDuration() + stillAnimationTime)) - stillAnimationTime);
    }

    public void render(GameState gameState) {
        if (isActive) {
            skeletons.forEach(s -> s.dispose(gameState));
            skeletons = new LinkedList<>();
        }
    }
}
