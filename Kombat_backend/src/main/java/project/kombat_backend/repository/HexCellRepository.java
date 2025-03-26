package project.kombat_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.kombat_backend.model.game.HexCell;
import java.util.List;

@Repository
public interface HexCellRepository extends JpaRepository<HexCell, Long> {
    List<HexCell> findByOwnerIsNull();
    List<HexCell> findByOwner_Id(Long playerId);
}
