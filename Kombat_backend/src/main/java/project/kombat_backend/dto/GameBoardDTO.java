package project.kombat_backend.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@Data
public class GameBoardDTO {
    private Long id;
    private int size;
    private PlayerDTO playerOne;
    private PlayerDTO playerTwo;
    private PlayerDTO currentPlayer;
    private List<HexCellDTO> hexCells;
}
