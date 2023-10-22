package pl.prasulakorpo.cyberwarriors.model;

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
    private StaticObject ground;
    private StaticObject leftWall;
    private StaticObject rightWall;
    private List<StaticObject> platforms = new ArrayList<>();
    private StaticObject background;
    private Player player;
    private Map<String, Player> players = new HashMap<>();
    private float stateTime;
    private DrawableManager drawableManager;

    public void updateStateTime(float time) {
        stateTime += time;
    }

}
