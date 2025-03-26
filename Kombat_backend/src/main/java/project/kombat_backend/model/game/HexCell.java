package project.kombat_backend.model.game;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.kombat_backend.dto.HexCellDTO;
import project.kombat_backend.model.minion.Minion;
import project.kombat_backend.model.player.Player;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class HexCell {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int x;
    private int y;

    @ManyToOne
    //@JoinColumn(name = "game_board_id")
    @JsonIgnore
    private GameBoard gameBoard;

    @ManyToOne
    //@JoinColumn(name = "player_id")
    @JsonIgnore
    private Player owner;

    @ManyToOne
    //@JoinColumn(name = "minion_id")
    @JsonIgnore
    private Minion minion;

    public HexCell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean isOccupied() {
        return owner != null;
    }

    public void removeMinion() {
        this.minion = null;
    }

    public boolean hasMinion() {
        return minion != null;
    }

    public boolean isBuyable() {
        return owner == null;  // สามารถซื้อได้ถ้ายังไม่มีเจ้าของ
    }

    public HexCellDTO toDTO() {
        HexCellDTO dto = new HexCellDTO();
        dto.setX(this.x);
        dto.setY(this.y);
        dto.setOwnerName(owner != null ? owner.getName() : null);
        dto.setHasMinion(minion != null);
        return dto;
    }

}
