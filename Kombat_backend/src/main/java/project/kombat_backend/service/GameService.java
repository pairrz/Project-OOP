package project.kombat_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.kombat_backend.config.GameConfig;
import project.kombat_backend.dto.MinionDTO;
import project.kombat_backend.model.game.GameMode;
import project.kombat_backend.model.game.HexCell;
import project.kombat_backend.model.websocket.GameState;
import project.kombat_backend.model.game.GameBoard;
import project.kombat_backend.model.minion.Minion;
import project.kombat_backend.model.player.Player;
import project.kombat_backend.repository.GameBoardRepository;
import project.kombat_backend.repository.HexCellRepository;
import project.kombat_backend.repository.MinionRepository;
import project.kombat_backend.repository.PlayerRepository;
import java.util.List;
import java.util.Optional;

@Service
public class GameService {

    @Autowired
    private GameBoardRepository gameBoardRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private HexCellRepository hexCellRepository;

    @Autowired
    private MinionRepository minionRepository;

    @Autowired
    private GameManager gameManager;

    // สร้างเกมใหม่
    public GameBoard createGame(String playerOneName, String playerTwoName, GameMode gameMode) {
        Player player1 = null;
        Player player2 = null;
        GameConfig gameConfig = gameManager.getGameConfig();

        try {
            // สร้าง Player ตาม GameMode ที่เลือก
            switch (gameMode) {
                case DUEL_MODE:
                    player1 = new Player(playerOneName, gameConfig);  // สร้าง Player 1
                    player2 = new Player(playerTwoName, gameConfig);  // สร้าง Player 2
                    break;
                case SOLITAIRE_MODE:
                    player1 = new Player(playerOneName, gameConfig);  // สร้าง Player 1
                    break;
                case AUTO_MODE:
                    player1 = new Player(playerOneName, gameConfig);  // สร้าง Player 1
                    player2 = new Player("AI", gameConfig);  // สร้าง AI
                    break;
                default:
                    throw new IllegalArgumentException("Invalid game mode");
            }

            // สร้าง GameBoard และเพิ่ม Player
            GameBoard gameBoard = new GameBoard(player1, player2, gameConfig);
            gameManager.createGame(gameBoard);  // เพิ่ม GameBoard ใหม่

            // เก็บ GameBoard ลง database
            return gameBoardRepository.save(gameBoard);

        } catch (Exception e) {
            // Log ข้อผิดพลาด
            e.printStackTrace();
            // เพิ่มการจัดการข้อผิดพลาด
            throw new RuntimeException("Error creating game: " + e.getMessage(), e);
        }
    }

    // ดึงเกมตาม ID
    public Optional<GameBoard> getGameById(Long gameId) {
        return gameBoardRepository.findById(gameId);
    }

    // เปลี่ยนเทิร์น
    public void nextTurn(Long gameId) {
        Optional<GameBoard> optionalGame = gameBoardRepository.findById(gameId);
        if (optionalGame.isPresent()) {
            GameBoard gameBoard = optionalGame.get();
            gameBoard.switchPlayers();
            gameBoardRepository.save(gameBoard);
            gameManager.nextTurn();
        }
    }

    public void buyMinion(Player player, Long minionId, Long targetHexCellId) {
        // ตรวจสอบว่า Player มีงบประมาณเพียงพอ
        Minion minion = minionRepository.findById(minionId).orElseThrow(() -> new IllegalStateException("Minion not found"));
        int cost = gameManager.getGameConfig().getSpawnCost();
        if (player.getBudget() < cost) {
            throw new IllegalStateException("Not enough budget to buy Minion.");
        }

        // ลดงบประมาณของ Player
        player.setBudget(player.getBudget() - cost);
        playerRepository.save(player);

        // ตรวจสอบตำแหน่งของ HexCell ที่จะวาง Minion
        HexCell targetHex = hexCellRepository.findById(targetHexCellId).orElseThrow(() -> new IllegalStateException("HexCell not found"));

        // วาง Minion ลงบน HexCell
        minion.setPosition(targetHex);
        minion.setOwner(player);
        minionRepository.save(minion);

        // เพิ่ม Minion ให้กับ Player
        player.getMinions().add(minion);
        playerRepository.save(player);
    }

    public void buyHexCell(Player player, Long hexCellId) {
        // ดึง HexCell จากฐานข้อมูล
        HexCell hexCell = hexCellRepository.findById(hexCellId).orElseThrow(() -> new IllegalStateException("HexCell not found"));

        if (!hexCell.isBuyable()) {
            throw new IllegalStateException("HexCell cannot be bought.");
        }

        int cost = gameManager.getGameConfig().getHexPurchaseCost();
        // ตรวจสอบงบประมาณของผู้เล่น
        if (player.getBudget() < cost) {
            throw new IllegalStateException("Not enough budget to buy HexCell.");
        }

        // ลดงบประมาณของ player
        player.setBudget(player.getBudget() - cost);
        playerRepository.save(player);

        // เพิ่ม HexCell ใน ownedHexCells ของ player
        player.getHexCells().add(hexCell);
        playerRepository.save(player);

        // อัปเดตสถานะการเป็นเจ้าของ HexCell
        hexCell.setOwner(player);
        hexCellRepository.save(hexCell);
    }

    public void updateMinionStats(MinionDTO minionStats) {
        Minion minion = minionRepository.findById(minionStats.getId())
                .orElseThrow(() -> new IllegalStateException("Minion not found"));

        // อัปเดตค่า HP และ DEF ของ Minion ตามที่ผู้เล่นส่งมา
        minion.setHp(minionStats.getHp());
        minion.setDef(minionStats.getDef());
        minionRepository.save(minion);
    }

    public void assignStrategyToMinion(Long minionId, String strategy) {
        Minion minion = minionRepository.findById(minionId)
                .orElseThrow(() -> new IllegalStateException("Minion not found"));

        // อัปเดต Strategy ให้กับ Minion
        minion.setStrategy(strategy);
        minionRepository.save(minion);
    }

    //ดึง Player ทั้งหมด
    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    //ดึง Minion ทั้งหมด
    public List<Minion> getAllMinions() {
        return minionRepository.findAll();
    }

    //เพิ่มให้ REST /api/init ใช้ดึงสถานะเกมปัจจุบัน
    public GameState getInitialState() {
        // ดึงสถานะเกมจาก GameManager หรือ GameBoard ล่าสุด
        GameBoard currentBoard = gameManager.getCurrentGame();
        return new GameState(currentBoard); // ดัดแปลง GameBoard -> Game
    }

}