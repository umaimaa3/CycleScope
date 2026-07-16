package backend.service;

import backend.model.CycleHistory;
import backend.repository.CycleHistoryRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;

@Service
public class CycleHistoryService {

    private final CycleHistoryRepository cycleHistoryRepository;

    public CycleHistoryService(CycleHistoryRepository cycleHistoryRepository) {
        this.cycleHistoryRepository = cycleHistoryRepository;
    }

    public CycleHistory saveCycleHistory(CycleHistory cycleHistory) {
        return cycleHistoryRepository.save(cycleHistory);
    }

    public List<CycleHistory> getAllCycles() {
        return cycleHistoryRepository.findAll();
    }

     public Optional<CycleHistory> getCycleById(Long id) {
        return cycleHistoryRepository.findById(id);
    }

    public Optional<CycleHistory> getLatestCycle() {
        return cycleHistoryRepository.findTopByOrderByCycleNumberDesc();
    }

}
