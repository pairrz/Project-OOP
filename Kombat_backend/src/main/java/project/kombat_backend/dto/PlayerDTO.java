package project.kombat_backend.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import project.kombat_backend.model.player.Player;

@Getter
@Setter
@Data
public class PlayerDTO {
    private Long id;
    private String name;
    private int budget;

    public PlayerDTO() {}

    public PlayerDTO(Player player) {
        this.id = player.getId();
        this.name = player.getName();
        this.budget = player.getBudget();
    }
}
