package project.kombat_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.kombat_backend.model.game.GameBoard;

import java.util.Optional;

@Repository
public interface GameBoardRepository extends JpaRepository<GameBoard, Long> {
    Optional<GameBoard> findById(Long id);
}

