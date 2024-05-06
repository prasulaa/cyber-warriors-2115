package pl.prasulakorpo.cyberwarriors.model;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public class PlayerAnimations implements Disposable {

    private final Animation<TextureRegion> standingPoseLeft;
    private final Animation<TextureRegion> standingPoseRight;
    private final Animation<TextureRegion> runLeft;
    private final Animation<TextureRegion> runRight;
    private final Animation<TextureRegion> attackLeft;
    private final Animation<TextureRegion> attackRight;

    @Override
    public void dispose() {
        dispose(standingPoseLeft.getKeyFrames());
        dispose(standingPoseRight.getKeyFrames());
        dispose(runLeft.getKeyFrames());
        dispose(runRight.getKeyFrames());
        dispose(attackLeft.getKeyFrames());
        dispose(attackRight.getKeyFrames());
    }

    private void dispose(TextureRegion[] regions) {
        Arrays.stream(regions).forEach(r -> r.getTexture().dispose());
    }

}
