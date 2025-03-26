package project.kombat_backend.model.player;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import project.kombat_backend.config.GameConfig;
import project.kombat_backend.dto.PlayerDTO;
import project.kombat_backend.model.game.GameBoard;
import project.kombat_backend.model.game.HexCell;
import project.kombat_backend.model.minion.Minion;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@Entity
@Table (name = "Player")
public class Player {
    @Getter
    @Transient
    private GameConfig gameConfig;

    private String name;
    private int budget;
    private int baseR;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "game_board_id")
    private GameBoard gameBoard;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<HexCell> hexCells;

    @JsonIgnore
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Minion> minions;

    public Player() {
    }

    public Player(String name,GameConfig gameConfig) {
        this.name = name;
        this.hexCells  = new HashSet<>();
        this.budget = gameConfig.getInitBudget();
        this.minions = new ArrayList<>();
        this.baseR = gameConfig.getInterestPct();
        this.gameConfig = gameConfig;
    }

    public PlayerDTO toDTO() {
        PlayerDTO dto = new PlayerDTO();
        dto.setId(this.id);
        dto.setName(this.name);
        dto.setBudget(this.budget);
        return dto;
    }

    public void removeMinion(Minion minion) {
        this.minions.remove(minion);
    }

    public int getNumMinions() {
        return this.minions.size();
    }

    public void resetBudget(int turn) {
        if (turn > 1) {
            budget += gameConfig.getTurnBudget();
            double rate = getRate(turn) / 100.0;
            budget = (int) Math.floor(budget + (budget * rate));
            budget =  Math.min(budget, gameConfig.getMaxBudget());
        }
    }

    public double getRate(int turn) {
        if (budget <= 0 || turn <= 1) return 0;
        return baseR * Math.log10(budget) * Math.log(turn);
    }

}
