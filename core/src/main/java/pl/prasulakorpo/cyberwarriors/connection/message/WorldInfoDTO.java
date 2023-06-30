package pl.prasulakorpo.cyberwarriors.connection.message;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class WorldInfoDTO extends GeneralMsg {
    public static final String NAME = "cw-worldinfo";

    private List<PlayerDTO> players;

}
