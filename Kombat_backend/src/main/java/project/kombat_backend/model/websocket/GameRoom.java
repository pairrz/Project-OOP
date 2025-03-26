package project.kombat_backend.model.websocket;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class GameRoom {
    private String roomId;
    private List<WebSocketSession> players = new ArrayList<>();  // เก็บ player 2 คน
}
