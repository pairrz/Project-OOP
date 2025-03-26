package project.kombat_backend.service;

import lombok.Getter;
import org.springframework.stereotype.Service;
import project.kombat_backend.config.GameConfig;
import project.kombat_backend.model.game.GameBoard;
import project.kombat_backend.model.player.Player;
import project.kombat_backend.model.player.BotPlayer;

import java.util.Objects;

@Service
public class GameManager {
    @Getter
    private final GameConfig gameConfig;
    private int turn = 1;
    private GameBoard gameBoard;

    public GameManager(GameConfig gameConfig) {
        this.gameConfig = gameConfig;
    }

    public void createGame(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
        this.turn = 1;
    }

    public GameBoard getGame(Long gameId) {
        if(Objects.equals(gameId, gameBoard.getId())) {
            return this.gameBoard;
        }
        return null;
    }

    public GameBoard getCurrentGame() {
        return this.gameBoard;
    }

    public void removeGame(Long gameId) {
        if(Objects.equals(gameId, gameBoard.getId())) {
            this.gameBoard = null;
        }
    }

    public void nextTurn() {
        turn++;
        if (gameBoard != null) {
            gameBoard.switchPlayers();
            handleBotTurn();  // เพิ่มให้บอททำงานอัตโนมัติ
        }
    }

    public int getCurrentTurn() {
        return turn;
    }

    public void resetPlayerBudget(Player player) {
        player.resetBudget(turn);
    }

    private void handleBotTurn() {
        Player currentPlayer = gameBoard.getCurrentPlayer();
        if (currentPlayer instanceof BotPlayer bot) {
            bot.takeTurn(turn);  // บอทรันกลยุทธ์ของตัวเอง
        }
    }
}
