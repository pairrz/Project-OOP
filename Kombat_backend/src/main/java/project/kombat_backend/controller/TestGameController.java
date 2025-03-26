package project.kombat_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project.kombat_backend.config.GameConfig;
import project.kombat_backend.dto.MinionDTO;
import project.kombat_backend.model.game.HexCell;
import project.kombat_backend.model.player.Player;
import project.kombat_backend.model.websocket.GameState;
import project.kombat_backend.model.minion.Minion;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/test")
public class TestGameController {

    @Autowired
    private GameConfig gameConfig;

    @GetMapping("/gamestate")
    public GameState getTestGameState() {
        GameState gameState = new GameState();
        gameState.setCurrentPlayer("Player 1");
        gameState.setAction("Test GameState");
        gameState.setPlayerStatus(500);

        List<Minion> minions = new ArrayList<>();
        Minion m1 = new Minion();
        HexCell cell1 = new HexCell(2, 3);
        m1.setId(1L);
        m1.setX(2);
        m1.setY(3);
        m1.setPosition(cell1);
        m1.setHp(gameConfig.getInitHp());  // ✅ ดึงค่า hp จาก config
        m1.setDef(gameConfig.getInitHp() / 2);
        minions.add(m1);

        Minion m2 = new Minion();
        HexCell cell2 = new HexCell(4, 5);
        m2.setId(2L);
        m2.setX(4);
        m2.setY(5);
        m2.setPosition(cell2);
        m2.setHp(gameConfig.getInitHp());
        m2.setDef(gameConfig.getInitHp() / 2);
        minions.add(m2);

        gameState.setMinionPositions(minions);
        return gameState;
    }

    @GetMapping("/minion/test")
    public MinionDTO testMinion() {
        Minion minion = new Minion();
        minion.setId(1L);
        minion.setHp(gameConfig.getInitHp());
        minion.setDef(gameConfig.getInitHp() / 2);
        minion.setX(3);
        minion.setY(4);
        Player p = new Player();
        p.setName("Player1");
        minion.setOwner(p);
        return minion.toDTO();
    }

    @GetMapping("/fullgame")
    public GameState testFullGame() {
        Player player = new Player();
        player.setId(1L);
        player.setName("Player 1");
        player.setBudget(gameConfig.getInitBudget());

        HexCell cell = new HexCell(1, 1);
        Minion minion = new Minion();
        minion.setId(1L);
        minion.setHp(gameConfig.getInitHp());
        minion.setDef(gameConfig.getInitHp() / 2);
        minion.setX(1);
        minion.setY(1);
        minion.setPosition(cell);
        minion.setOwner(player);

        List<Minion> minions = new ArrayList<>();
        minions.add(minion);
        player.setMinions(minions);

        GameState gameState = new GameState();
        gameState.setCurrentPlayer(player.getName());
        gameState.setAction("Full Game Mock Test");
        gameState.setPlayerStatus(player.getBudget());
        gameState.setMinionPositions(minions);

        return gameState;
    }
}
