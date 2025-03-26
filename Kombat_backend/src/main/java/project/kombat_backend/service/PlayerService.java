package project.kombat_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.kombat_backend.model.player.Player;
import project.kombat_backend.repository.PlayerRepository;
import java.util.List;

@Service
public class PlayerService {
    @Autowired
    private PlayerRepository playerRepository;

    public Player getPlayerById(Long id) {
        return playerRepository.findById(id).orElse(null);
    }

    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    public Player savePlayer(Player player) {
        return playerRepository.save(player);
    }

    public void deletePlayer(Long id) {
        playerRepository.deleteById(id);
    }
}