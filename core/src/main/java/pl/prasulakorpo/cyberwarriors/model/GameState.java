package pl.prasulakorpo.cyberwarriors.model;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.prasulakorpo.cyberwarriors.drawing.DrawableManager;

import java.util.*;

@NoArgsConstructor
@Setter
@Getter
public class GameState {

    private World world;
    private List<StaticObject> platforms = new ArrayList<>();
    private StaticObject background;
    private Player player1;
    private Player player2;
    private List<Skeleton> skeletons = new LinkedList<>();
    private List<Effect> effects = new LinkedList<>();
    private List<Tower> towers = new LinkedList<>();
    private float stateTime;
    private DrawableManager drawableManager;
    private int skeletonCounter;
    private Sound music;

    public void updateStateTime(float time) {
        stateTime += time;
    }

    public void incrementSkeletons() {
        skeletonCounter++;
    }

}
