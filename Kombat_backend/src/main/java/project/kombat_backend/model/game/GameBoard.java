package project.kombat_backend.model.game;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.kombat_backend.config.GameConfig;
import project.kombat_backend.dto.GameBoardDTO;
import project.kombat_backend.model.player.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class GameBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private final int size = 8;

    @OneToOne(cascade = CascadeType.ALL)
    private Player playerOne;

    @OneToOne(cascade = CascadeType.ALL)
    private Player playerTwo;

    @OneToOne
    private Player currentPlayer;

    @Transient
    private Player opponentPlayer;

    @Transient
    private GameConfig gameConfig;

    @OneToMany(mappedBy = "gameBoard", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<HexCell> hexCellMap = new HashSet<>();

    @ElementCollection
    private List<Long> player1Hexes = new ArrayList<>();

    @ElementCollection
    private List<Long> player2Hexes = new ArrayList<>();

    public GameBoard(Player playerOne,Player playerTwo,GameConfig gameConfig) {
        this.gameConfig = gameConfig;
        this.currentPlayer = playerOne;
        this.opponentPlayer = playerTwo;

        setBoard();
        setupPlayerOneHexes();
        setupPlayerTwoHexes();
    }

    private void setBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                HexCell cell = new HexCell(i, j);
                hexCellMap.add(cell);
            }
        }
    }

    public void setupPlayerOneHexes() {
        int k = 3;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < k; j++) {
                HexCell cell = getHexCellAt(i, j);
                if (cell != null) {
                    player1Hexes.add(cell.getId());
                    cell.setOwner(playerOne);
                }
            }
            k--;
        }
    }

    public void setupPlayerTwoHexes() {
        int k = 5;
        for (int i = 7; i >= 6; i--) {
            for (int j = k; j < size; j++) {
                HexCell cell = getHexCellAt(i, j);
                if (cell != null) {
                    player2Hexes.add(cell.getId());
                    cell.setOwner(playerTwo);
                }
            }
            k++;
        }
    }

    public HexCell getHexCellAt(int x, int y) {
        return hexCellMap.stream()
                .filter(cell -> cell.getX() == x && cell.getY() == y)
                .findFirst()
                .orElse(null);
    }

    public boolean isValidPosition(int x, int y) {
        return getHexCellAt(x, y) != null;
    }

    public int getSpawnRemaining() {
        if (gameConfig == null) {
            throw new IllegalStateException("GameConfig not set in GameBoard");
        }
        return gameConfig.getMaxSpawns() - (playerOne.getNumMinions() + playerTwo.getNumMinions());
    }

    public void switchPlayers() {
        Player temp = currentPlayer;
        currentPlayer = opponentPlayer;
        opponentPlayer = temp;
    }

    public GameBoardDTO toDTO() {
        GameBoardDTO dto = new GameBoardDTO();
        dto.setId(this.id);
        dto.setSize(this.size);
        dto.setPlayerOne(this.playerOne != null ? this.playerOne.toDTO() : null);
        dto.setPlayerTwo(this.playerTwo != null ? this.playerTwo.toDTO() : null);
        dto.setCurrentPlayer(this.currentPlayer != null ? this.currentPlayer.toDTO() : null);
        dto.setHexCells(this.hexCellMap.stream().map(HexCell::toDTO).toList());
        return dto;
    }

    public List<Integer> getSelectedMinions() {
        return getSelectedMinions();
    }
}