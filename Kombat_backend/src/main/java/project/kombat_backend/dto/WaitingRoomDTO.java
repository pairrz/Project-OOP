package project.kombat_backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WaitingRoomDTO {
    private String roomCode;
    private PlayerDTO playerOne;
    private PlayerDTO playerTwo;

    public WaitingRoomDTO(String roomCode, PlayerDTO playerOne, PlayerDTO playerTwo) {
        this.roomCode = roomCode;
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
    }
}
