package project.kombat_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import project.kombat_backend.dto.*;
import project.kombat_backend.model.game.GameBoard;
import project.kombat_backend.model.player.Player;
import project.kombat_backend.model.websocket.GameState;
import project.kombat_backend.repository.PlayerRepository;
import project.kombat_backend.service.GameService;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@RequestMapping("/api/game")
public class GameController {

    @Autowired
    private GameService gameService;

    @Autowired
    private PlayerRepository playerRepository;

    // รวม getInitialGameState() ที่ดึงค่าเริ่มต้นของเกม
    @GetMapping("/init")
    public GameState getInitialGameState() {
        return gameService.getInitialState();
    }

    // API สำหรับ "สร้างเกม"
    @PostMapping("/create")
    public ResponseEntity<GameBoardDTO> createGame(@RequestBody GameRequest request) {
        try {
            GameBoard gameBoard = gameService.createGame(
                    request.getPlayerOneName(),
                    request.getPlayerTwoName(),
                    request.getGameMode());

            GameBoardDTO gameBoardDTO = new GameBoardDTO();
            gameBoardDTO.setId(gameBoard.getId());
            gameBoardDTO.setSize(8);
            gameBoardDTO.setPlayerOne(new PlayerDTO(gameBoard.getPlayerOne()));
            gameBoardDTO.setPlayerTwo(new PlayerDTO(gameBoard.getPlayerTwo()));
            gameBoardDTO.setCurrentPlayer(new PlayerDTO(gameBoard.getCurrentPlayer()));

            List<HexCellDTO> hexCellDTOs = gameBoard.getHexCellMap().stream()
                    .map(HexCellDTO::new)
                    .toList();
            gameBoardDTO.setHexCells(hexCellDTOs);

            return ResponseEntity.ok(gameBoardDTO);
        } catch (Exception e) {
            // log the error for debugging
            e.printStackTrace();
            // return a 500 error response in case of failure
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // API ดึงข้อมูลเกม
    @GetMapping("/{gameId}")
    public ResponseEntity<GameBoardDTO> getGame(@PathVariable Long gameId) {
        Optional<GameBoard> game = gameService.getGameById(gameId);
        return game.map(g -> ResponseEntity.ok(g.toDTO()))
                .orElse(ResponseEntity.notFound().build());
    }

    // API เปลี่ยนเทิร์น
    @PostMapping("/{gameId}/next-turn")
    public ResponseEntity<String> nextTurn(@PathVariable Long gameId) {
        gameService.nextTurn(gameId);
        return ResponseEntity.ok("Turn changed successfully");
    }

    // WebSocket ระบบ Turn-Based
    private String currentTurn = "Player1";

    @MessageMapping("/action")
    @SendTo("/topic/game-state")
    public GameState handleAction(GameState message){
        System.out.println("Received Action: " + message.getAction() + " by " + message.getCurrentPlayer());

        if (!message.getCurrentPlayer().equals(currentTurn)) {
            return new GameState(currentTurn, "Not your turn");
        }

        if ("endTurn".equals(message.getAction())) {
            currentTurn = currentTurn.equals("Player1") ? "Player2" : "Player1";
        }

        return new GameState(currentTurn, message.getAction());
    }

    //API กำหนดกลยุทธ์ให้ Minion
    @PostMapping("/assignStrategy")
    public ResponseEntity<String> assignStrategy(@RequestBody MinionDTO minionDTO) {
        String strategy = minionDTO.getStrategy();
        Long minionId = minionDTO.getId();

        if (strategy == null || strategy.trim().isEmpty()) {
            strategy = loadDefaultStrategy();
        }

        try {
            gameService.assignStrategyToMinion(minionId, strategy);
            return ResponseEntity.ok("Strategy assigned successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    private String loadDefaultStrategy() {
        Path path = Paths.get("D:\\Kombat_backend\\src\\main\\resources\\config\\strategy.txt");
        StringBuilder fullContent = new StringBuilder();

        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    fullContent.append(line).append(" ");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (fullContent.isEmpty()) {
            System.err.println("Strategy file is empty or only contains whitespace.");
        }

        return fullContent.toString();
    }

    @PostMapping("/buyHex/{playerId}/{hexCellId}")
    public Map<String, String> buyHexCell(@PathVariable Long playerId, @PathVariable Long hexCellId) {
        Map<String, String> response = new HashMap<>();
        Player player = playerRepository.findById(playerId).orElseThrow(() -> new IllegalStateException("Player not found"));

        try {
            gameService.buyHexCell(player, hexCellId);
            response.put("status", "success");
            response.put("message", "HexCell purchased successfully.");
        } catch (Exception e) {
            response.put("status", "fail");
            response.put("message", "Error: " + e.getMessage());
        }

        return response;
    }

    @PostMapping("/buyMinion/{playerId}/{minionId}/{hexCellId}")
    public Map<String, String> buyMinion(@PathVariable Long playerId, @PathVariable Long minionId, @PathVariable Long hexCellId) {
        Map<String, String> response = new HashMap<>();
        Player player = playerRepository.findById(playerId).orElseThrow(() -> new IllegalStateException("Player not found"));

        try {
            gameService.buyMinion(player, minionId, hexCellId);
            response.put("status", "success");
            response.put("message", "Minion purchased successfully.");
        } catch (Exception e) {
            response.put("status", "fail");
            response.put("message", "Error: " + e.getMessage());
        }

        return response;
    }
}
