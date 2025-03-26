package project.kombat_backend.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import project.kombat_backend.model.game.HexCell;

@Getter
@Setter
@Data
public class HexCellDTO {
    private int x;
    private int y;
    private String ownerName;
    private boolean hasMinion;

    public HexCellDTO(){}

    public HexCellDTO(HexCell hexCell) {
        this.x = hexCell.getX();
        this.y = hexCell.getY();
        this.ownerName = hexCell.getOwner().getName();
        this.hasMinion = hexCell.hasMinion();
    }
}
