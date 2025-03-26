package project.kombat_backend.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import project.kombat_backend.model.websocket.GameRoom;
import project.kombat_backend.model.game.GameBoard;
import project.kombat_backend.service.GameService;
import project.kombat_backend.service.RoomManager;
import project.kombat_backend.service.GameManager;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class GameWebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private GameService gameService;

    private final RoomManager roomManager;
    private final GameManager gameManager;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private static final Map<String, String> sessionRoomMap = new ConcurrentHashMap<>();

    public GameWebSocketHandler(RoomManager roomManager, GameManager gameManager) {
        this.roomManager = roomManager;
        this.gameManager = gameManager;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        String uri = session.getUri() != null ? session.getUri().toString() : "";

        if (uri.contains("create")) {
            String newRoomId = UUID.randomUUID().toString();
            roomManager.createRoom(newRoomId);
            session.sendMessage(new TextMessage("roomCreated:" + newRoomId));
            return;
        }

        String roomId = uri.substring(uri.lastIndexOf("/") + 1);
        GameRoom room = roomManager.getRoom(roomId);
        if (room != null) {
            room.getPlayers().add(session);
            sessionRoomMap.put(session.getId(), roomId);
            sessions.put(session.getId(), session);

            //  ส่งสถานะเกมให้คนที่เพิ่งเข้าห้อง
            GameBoard game = gameManager.getGame(Long.parseLong(roomId));
            if (game != null) {
                broadcastGameState(roomId, game);
            }

            session.sendMessage(new TextMessage("joinedRoom:" + roomId));
        } else {
            session.sendMessage(new TextMessage("error:Room not found"));
        }
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        String roomId = sessionRoomMap.get(session.getId());
        if (roomId == null) return;

        String payload = message.getPayload();
        String[] parts = payload.split(":", 3);
        if (parts.length < 2) return;

        String gameIdStr = parts[0];
        String command = parts[1];
        Long targetId = (parts.length == 3) ? Long.parseLong(parts[2]) : null;

        long gameId;
        try {
            gameId = Long.parseLong(gameIdStr);
        } catch (NumberFormatException e) {
            session.sendMessage(new TextMessage("error:Invalid gameId"));
            return;
        }

        GameBoard game = gameManager.getGame(gameId);
        if (game == null) {
            session.sendMessage(new TextMessage("error:Game not found"));
            return;
        }

        switch (command) {
            case "buyHex":
                if (targetId != null) {
                    gameService.buyHexCell(game.getCurrentPlayer(), targetId);
                    broadcastGameState(roomId, game);
                }
                break;
            case "switch":
                game.switchPlayers();
                broadcastGameState(roomId, game);
                break;
            case "buyMinion":
                if (parts.length == 4) {
                    Long minionId = Long.parseLong(parts[2]);
                    Long targetHexCellId = Long.parseLong(parts[3]);
                    gameService.buyMinion(game.getCurrentPlayer(), minionId, targetHexCellId);
                    broadcastGameState(roomId, game);
                }
                break;
            default:
                session.sendMessage(new TextMessage("error:Unknown command"));
        }
    }

    private void broadcastGameState(String roomId, GameBoard game) throws IOException {
        String gameStateJson = objectMapper.writeValueAsString(game);
        GameRoom room = roomManager.getRoom(roomId);
        if (room != null) {
            for (WebSocketSession player : room.getPlayers()) {
                if (player.isOpen()) {
                    player.sendMessage(new TextMessage("gameState:" + gameStateJson));
                }
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session.getId());
        String roomId = sessionRoomMap.remove(session.getId());
        if (roomId != null) {
            GameRoom room = roomManager.getRoom(roomId);
            if (room != null) {
                room.getPlayers().remove(session);
            }
        }
    }
}
