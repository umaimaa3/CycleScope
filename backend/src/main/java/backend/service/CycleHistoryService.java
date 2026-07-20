package backend.service;

import backend.model.CycleHistory;
import backend.model.CycleStatus;
import backend.repository.CycleHistoryRepository;
import org.springframework.stereotype.Service;


import java.util.Optional;
import java.time.LocalDate;
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

    public CycleHistory updateCycleStatus(CycleHistory cycleHistory) {

        LocalDate today = LocalDate.now();

        if (
            cycleHistory.getStatus() == CycleStatus.PREDICTED &&
            !today.isBefore(cycleHistory.getPredictedStartDate())
        ) {
            cycleHistory.setStatus(CycleStatus.WAITING_FOR_START_CONFIRMATION);
        }

        return cycleHistoryRepository.save(cycleHistory);
    }

    public Optional<CycleHistory> getCurrentCycle() {

        Optional<CycleHistory> currentCycle = getLatestCycle();

        if (currentCycle.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(updateCycleStatus(currentCycle.get()));
    }

    private int getNextCycleNumber() {
        return getLatestCycle()
                .map(cycle -> cycle.getCycleNumber() + 1)
                .orElse(1);
    }

    private boolean predictionAlreadyExists(LocalDate predictedStartDate) {

        return getLatestCycle()
                .map(cycle -> cycle.getPredictedStartDate().equals(predictedStartDate))
                .orElse(false);
    }

    public void recordPredictedCycle(
        LocalDate predictedStartDate,
        LocalDate predictedEndDate,
        int predictedCycleLength,
        int predictedPeriodLength,
        Integer confidence) {

        if (predictionAlreadyExists(predictedStartDate)) {
            return;
        }

        CycleHistory cycleHistory = new CycleHistory(
                getNextCycleNumber(),
                predictedStartDate,
                predictedEndDate,
                predictedCycleLength,
                predictedPeriodLength,
                confidence
        );

        saveCycleHistory(cycleHistory);
    }

}
