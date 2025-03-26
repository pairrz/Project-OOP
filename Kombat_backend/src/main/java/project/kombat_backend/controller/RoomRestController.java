package project.kombat_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.kombat_backend.config.GameConfig;
import project.kombat_backend.dto.PlayerDTO;
import project.kombat_backend.dto.WaitingRoomDTO;
import project.kombat_backend.model.game.GameBoard;
import project.kombat_backend.model.player.Player;
import project.kombat_backend.service.RoomManager;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/room")
public class RoomRestController {

    @Autowired
    private RoomManager roomManager;

    @Autowired
    private GameConfig gameConfig;

    private final Map<String, WaitingRoomDTO> waitingRooms = new HashMap<>();
    private final Map<String, String> roomPlayers = new HashMap<>(); // บันทึกผู้เล่นในห้อง

    // API สำหรับ "สร้างห้อง"
    @PostMapping("/create")
    public Map<String, String> createRoom(@RequestParam String playerName) {
        String roomId = UUID.randomUUID().toString();
        roomManager.createRoom(roomId);
        roomPlayers.put(roomId, playerName); // บันทึกชื่อผู้เล่นคนแรก

        Map<String, String> response = new HashMap<>();
        response.put("roomId", roomId);
        return response;
    }

    @PostMapping("/join")
    public ResponseEntity<WaitingRoomDTO> joinRoom(@RequestParam String roomCode, @RequestParam String playerName) {
        WaitingRoomDTO room = Optional.ofNullable(waitingRooms.get(roomCode))
                .orElseThrow(() -> new RuntimeException("Room not found!"));

        if (room.getPlayerTwo() != null) {
            return ResponseEntity.badRequest().body(null); // ห้องเต็มแล้ว
        }

        Player playerTwo = new Player(playerName, gameConfig);
        room.setPlayerTwo(new PlayerDTO(playerTwo));

        // ใช้ Player ที่มีอยู่ใน room.getPlayerOne() แทนการสร้างใหม่
        Player playerOne = new Player(room.getPlayerOne().getName(), gameConfig);
        GameBoard gameBoard = new GameBoard(playerOne, playerTwo,gameConfig);

        return ResponseEntity.ok(room);
    }

    @GetMapping("/check/{roomId}")
    public Map<String, Boolean> checkRoom(@PathVariable String roomId) {
        boolean exists = roomManager.roomExists(roomId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return response;
    }
}
