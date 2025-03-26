package project.kombat_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.kombat_backend.model.minion.Minion;
import project.kombat_backend.repository.MinionRepository;
import java.util.List;

@Service
public class MinionService {
    @Autowired
    private MinionRepository minionRepository;

    public Minion getMinionById(Long id) {
        return minionRepository.findById(id).orElse(null);
    }

    public List<Minion> getMinionsByPlayerId(Long playerId) {
        return minionRepository.findByOwner_Id(playerId);
    }

    public Minion saveMinion(Minion minion) {
        return minionRepository.save(minion);
    }

    public void deleteMinion(Long id) {
        minionRepository.deleteById(id);
    }
}
