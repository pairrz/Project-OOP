package project.kombat_backend.model.minion;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import project.kombat_backend.config.GameConfig;
import project.kombat_backend.dto.MinionDTO;
import project.kombat_backend.model.game.HexCell;
import project.kombat_backend.model.player.Player;

@Setter
@Getter
@Entity
public class Minion {
    @Transient
    private GameConfig gameConfig;
    @Transient
    private String strategy;

    private int hp;
    private int def = 0;
    private int x;
    private int y;
    private int bonusDef;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    //@JoinColumn(name = "position_id", referencedColumnName = "id")
    private HexCell position;

    @ManyToOne
    @JoinColumn(name = "player_id", referencedColumnName = "id")
    private Player owner;

    public Minion() {}

    // Constructor ดึงค่าเริ่มต้นจาก GameConfig
    public Minion(Player owner, HexCell position, GameConfig gameConfig) {
        this.gameConfig = gameConfig;
        this.position = position;
        this.x = position.getX();
        this.y = position.getY();
        this.owner = owner;

        // ดึงค่า initHp และ defense จาก config
        this.hp = gameConfig.getInitHp();
        this.def += bonusDef;         // ตัวอย่าง: def เริ่มต้นเป็นครึ่งหนึ่งของ hp
        this.bonusDef = 10;
    }

    public MinionDTO toDTO() {
        MinionDTO dto = new MinionDTO();
        dto.setId(this.id);
        dto.setHp(this.hp);
        dto.setDef(this.def);
        dto.setX(this.x);
        dto.setY(this.y);
        dto.setOwnerName(owner != null ? owner.getName() : null);
        return dto;
    }
}
