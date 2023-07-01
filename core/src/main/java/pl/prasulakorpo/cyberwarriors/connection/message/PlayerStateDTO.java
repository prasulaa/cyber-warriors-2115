package pl.prasulakorpo.cyberwarriors.connection.message;

import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class PlayerStateDTO extends GeneralMsg {
    public static final String NAME = "cw-playerstate";

    private float x;
    private float y;
}
