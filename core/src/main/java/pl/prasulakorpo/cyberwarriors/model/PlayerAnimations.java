package pl.prasulakorpo.cyberwarriors.model;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PlayerAnimations {

    private final Animation<TextureRegion> standingPoseLeft;
    private final Animation<TextureRegion> standingPoseRight;
    private final Animation<TextureRegion> runLeft;
    private final Animation<TextureRegion> runRight;
    private final Animation<TextureRegion> jumpUpLeft;
    private final Animation<TextureRegion> jumpUpRight;
    private final Animation<TextureRegion> jumpDownLeft;
    private final Animation<TextureRegion> jumpDownRight;

}
