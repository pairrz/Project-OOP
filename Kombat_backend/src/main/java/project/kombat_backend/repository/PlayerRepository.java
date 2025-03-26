package project.kombat_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.kombat_backend.model.player.Player;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    Player findByName(String name);
}
