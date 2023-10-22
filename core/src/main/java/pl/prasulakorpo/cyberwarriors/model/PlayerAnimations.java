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
    private final Animation<TextureRegion> jumpUpLeft;
    private final Animation<TextureRegion> jumpUpRight;
    private final Animation<TextureRegion> jumpDownLeft;
    private final Animation<TextureRegion> jumpDownRight;

    @Override
    public void dispose() {
        dispose(standingPoseLeft.getKeyFrames());
        dispose(standingPoseRight.getKeyFrames());
        dispose(runLeft.getKeyFrames());
        dispose(runRight.getKeyFrames());
        dispose(jumpUpLeft.getKeyFrames());
        dispose(jumpUpRight.getKeyFrames());
        dispose(jumpDownLeft.getKeyFrames());
        dispose(jumpUpRight.getKeyFrames());
    }

    private void dispose(TextureRegion[] regions) {
        Arrays.stream(regions).forEach(r -> r.getTexture().dispose());
    }

}
