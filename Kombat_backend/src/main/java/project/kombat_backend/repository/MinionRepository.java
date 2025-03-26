package project.kombat_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.kombat_backend.model.minion.Minion;
import java.util.List;

@Repository
public interface MinionRepository extends JpaRepository<Minion, Long> {
    List<Minion> findByOwner_Id(Long playerId);
}