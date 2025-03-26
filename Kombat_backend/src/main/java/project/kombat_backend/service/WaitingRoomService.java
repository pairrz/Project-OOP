package project.kombat_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.kombat_backend.config.GameConfig;
import project.kombat_backend.dto.PlayerDTO;
import project.kombat_backend.dto.WaitingRoomDTO;
import project.kombat_backend.model.game.GameBoard;
import project.kombat_backend.model.player.Player;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class WaitingRoomService {
    @Autowired
    private GameConfig gameConfig;

    private final Map<String, WaitingRoomDTO> waitingRooms = new HashMap<>();

    public WaitingRoomDTO createRoom(String playerOneName) {
        String roomCode = UUID.randomUUID().toString().substring(0, 6);
        Player playerOne = new Player(playerOneName, gameConfig);
        WaitingRoomDTO room = new WaitingRoomDTO(roomCode, new PlayerDTO(playerOne), null);
        waitingRooms.put(roomCode, room);
        return room;
    }

    public WaitingRoomDTO joinRoom(String roomCode, String playerTwoName) {
        WaitingRoomDTO room = Optional.ofNullable(waitingRooms.get(roomCode))
                .orElseThrow(() -> new RuntimeException("Room not found!"));

        Player playerOne = new Player(room.getPlayerOne().getName(), gameConfig); //สร้าง Player จาก PlayerDTO
        Player playerTwo = new Player(playerTwoName, gameConfig);
        room.setPlayerTwo(new PlayerDTO(playerTwo));

        // สร้างเกมเมื่อผู้เล่นครบ 2 คน
        new GameBoard(playerOne, playerTwo,gameConfig);

        return room;
    }
}
