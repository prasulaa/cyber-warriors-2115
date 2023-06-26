package pl.prasulakorpo.cyberwarriors.connection.message;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PlayerDTO extends GeneralMsg {
    public static final String NAME = "cw-player";

    private String id;
    private float x;
    private float y;
}
