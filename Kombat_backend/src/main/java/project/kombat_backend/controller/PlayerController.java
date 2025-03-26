package project.kombat_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.kombat_backend.dto.PlayerDTO;
import project.kombat_backend.model.player.Player;
import project.kombat_backend.service.PlayerService;

import java.util.List;

@RestController
@RequestMapping("/api/player")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @GetMapping("/all")
    public ResponseEntity<List<PlayerDTO>> getAllPlayers() {
        List<PlayerDTO> players = playerService.getAllPlayers()
                .stream()
                .map(Player::toDTO)
                .toList();
        return ResponseEntity.ok(players);
    }

    @GetMapping("/{playerId}")
    public ResponseEntity<PlayerDTO> getPlayerById(@PathVariable Long playerId) {
        Player player = playerService.getPlayerById(playerId);
        return player != null ? ResponseEntity.ok(player.toDTO()) : ResponseEntity.notFound().build();
    }
}
