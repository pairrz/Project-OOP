package project.kombat_backend.service;

import org.springframework.stereotype.Service;
import project.kombat_backend.model.websocket.GameRoom;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RoomManager {

    private final Map<String, GameRoom> rooms = new ConcurrentHashMap<>();

    // สร้างห้องใหม่
    public void createRoom(String roomId) {
        GameRoom room = new GameRoom();
        rooms.put(roomId, room);
        System.out.println("Room Created: " + roomId);
    }

    // ดึงห้อง
    public GameRoom getRoom(String roomId) {
        return rooms.get(roomId);
    }

    // ลบห้อง (optional)
    public void removeRoom(String roomId) {
        rooms.remove(roomId);
        System.out.println("Room Removed: " + roomId);
    }

    // เช็คว่าห้องมีมั้ย
    public boolean roomExists(String roomId) {
        return rooms.containsKey(roomId);
    }

    // สำหรับ debug
    public Map<String, GameRoom> getAllRooms() {
        return rooms;
    }
}
