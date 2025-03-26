package project.kombat_backend.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import project.kombat_backend.model.game.GameMode;

@Getter
@Setter
@Data
public class GameRequest {
    private String playerOneName;
    private String playerTwoName;
    private GameMode gameMode;
}
