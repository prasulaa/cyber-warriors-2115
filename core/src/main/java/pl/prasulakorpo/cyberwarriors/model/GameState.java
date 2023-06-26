package pl.prasulakorpo.cyberwarriors.model;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class GameState {

    private World world;
    private Fixture ground;
    private Player player;
    private float stateTime;

    public void updateStateTime(float time) {
        stateTime += time;
    }

}
