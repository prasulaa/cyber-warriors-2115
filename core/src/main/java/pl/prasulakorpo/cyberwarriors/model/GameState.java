package pl.prasulakorpo.cyberwarriors.model;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@NoArgsConstructor
@Setter
@Getter
public class GameState {

    private World world;
    private Fixture ground;
    private Fixture background;
    private Player player;
    private Map<String, Player> players = new HashMap<>();
    private float stateTime;

    public void updateStateTime(float time) {
        stateTime += time;
    }

}
