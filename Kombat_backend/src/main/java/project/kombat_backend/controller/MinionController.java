package project.kombat_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.kombat_backend.dto.MinionDTO;
import project.kombat_backend.model.minion.Minion;
import project.kombat_backend.model.player.Player;
import project.kombat_backend.repository.PlayerRepository;
import project.kombat_backend.service.GameService;
import project.kombat_backend.service.MinionService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/minion")
public class MinionController {

    @Autowired
    private MinionService minionService;

    @Autowired
    private GameService gameService;

    @Autowired
    private PlayerRepository playerRepository;

    @GetMapping("/all")
    public ResponseEntity<List<MinionDTO>> getAllMinions(@RequestParam Long playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new IllegalStateException("Player not found"));

        // ดึง Minion ของ Player ที่ระบุ
        List<MinionDTO> minions = player.getMinions().stream()
                .map(Minion::toDTO)  // แปลง Minion เป็น MinionDTO
                .toList();

        return ResponseEntity.ok(minions);
    }

    @GetMapping("/{minionId}")
    public ResponseEntity<MinionDTO> getMinionById(@PathVariable Long minionId) {
        Minion minion = minionService.getMinionById(minionId);
        return minion != null ? ResponseEntity.ok(minion.toDTO()) : ResponseEntity.notFound().build();
    }

    @GetMapping("/player/{playerId}")
    public ResponseEntity<List<MinionDTO>> getMinionsByPlayer(@PathVariable Long playerId) {
        List<MinionDTO> minions = minionService.getMinionsByPlayerId(playerId)
                .stream()
                .map(Minion::toDTO)
                .toList();
        return ResponseEntity.ok(minions);
    }

    // API สำหรับการอัปเดต HP และ DEF ของ Minion
    @PostMapping("/assign")
    public Map<String, String> assignMinionStats(@RequestBody MinionDTO minionStats) {
        Map<String, String> response = new HashMap<>();

        try {
            gameService.updateMinionStats(minionStats);
            response.put("status", "success");
            response.put("message", "Minion stats updated successfully.");
        } catch (Exception e) {
            response.put("status", "fail");
            response.put("message", "Error: " + e.getMessage());
        }

        return response;
    }

    @PostMapping("/selectMinions")
    public ResponseEntity<Map<String, String>> selectMinions(@RequestBody Map<String, Object> request) {
        List<Integer> selectedMinions = (List<Integer>) request.get("selectedMinions");

        try {
            // สร้างมินเนียนตามที่เลือก
            //gameService.assignMinionsToGame(selectedMinions);

            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Minions selected successfully.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "fail");
            response.put("message", "Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

//    // API สำหรับการตั้งค่า Strategy ให้กับ Minion
//    @PostMapping("/assignStrategy")
//    public Map<String, String> assignStrategy(@RequestBody MinionStrategyDTO minionStrategyDTO) {
//        Map<String, String> response = new HashMap<>();
//
//        try {
//            // เรียก GameService เพื่ออัปเดต Strategy ให้กับ Minion
//            gameService.assignStrategyToMinion(minionStrategyDTO);
//            response.put("status", "success");
//            response.put("message", "Strategy assigned to Minion successfully.");
//        } catch (Exception e) {
//            response.put("status", "fail");
//            response.put("message", "Error: " + e.getMessage());
//        }
//
//        return response;
//    }
}
