package project.kombat_backend.model.websocket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.kombat_backend.model.game.GameBoard;
import project.kombat_backend.model.minion.Minion;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameState {
    private String currentPlayer;
    private String action;

    @Setter
    private int playerStatus;
    @Setter
    private List<Minion> minionPositions;         // list minion พร้อมตำแหน่งปัจจุบัน

    public GameState(GameBoard gameBoard) {
        if (gameBoard != null) {
            this.currentPlayer = gameBoard.getCurrentPlayer().getName();
            this.action = "Game Initialized";
        } else {
            this.currentPlayer = "No Game";
            this.action = "No Action";
        }
    }

    public GameState(String currentPlayer, String action) {
        this.currentPlayer = currentPlayer;
        this.action = action;
    }

}
