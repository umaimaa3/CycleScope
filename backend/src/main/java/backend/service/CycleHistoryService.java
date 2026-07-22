package backend.service;

import backend.model.CycleHistory;
import backend.model.CycleStatus;
import backend.repository.CycleHistoryRepository;
import org.springframework.stereotype.Service;


import java.util.Optional;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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

        if (
            cycleHistory.getStatus() == CycleStatus.ACTIVE &&
            !today.isBefore(cycleHistory.getPredictedEndDate())
        ) {
            cycleHistory.setStatus(CycleStatus.WAITING_FOR_END_CONFIRMATION);
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

    private Optional<CycleHistory> getCycleInStatus(Long id, CycleStatus expectedStatus) {

        Optional<CycleHistory> optionalCycle = cycleHistoryRepository.findById(id);

        if (optionalCycle.isEmpty()) {
            return Optional.empty();
        }

        CycleHistory cycle = optionalCycle.get();

        if (cycle.getStatus() != expectedStatus) {
            return Optional.empty();
        }

        return Optional.of(cycle);
    }

    public Optional<CycleHistory> confirmPeriodStart(Long id, LocalDate actualStartDate) {

        Optional<CycleHistory> optionalCycle =
                getCycleInStatus(
                        id,
                        CycleStatus.WAITING_FOR_START_CONFIRMATION
                );

        if (optionalCycle.isEmpty()) {
            return Optional.empty();
        }

        CycleHistory cycle = optionalCycle.get();

        cycle.setActualStartDate(actualStartDate);

        long error = ChronoUnit.DAYS.between(
                cycle.getPredictedStartDate(),
                actualStartDate
        );

        cycle.setPredictionErrorDays((int) error);

        cycle.setStatus(CycleStatus.ACTIVE);

        cycleHistoryRepository.save(cycle);

        return Optional.of(cycle);
    }

    public Optional<CycleHistory> confirmPeriodEnd(Long id, LocalDate actualEndDate) {

        Optional<CycleHistory> optionalCycle =
                getCycleInStatus(
                        id,
                        CycleStatus.WAITING_FOR_END_CONFIRMATION
                );

        if (optionalCycle.isEmpty()) {
            return Optional.empty();
        }

        CycleHistory cycle = optionalCycle.get();

        cycle.setActualEndDate(actualEndDate);

        long periodLength = ChronoUnit.DAYS.between(
                cycle.getActualStartDate(),
                actualEndDate
        ) + 1;

        cycle.setActualPeriodLength((int) periodLength);

        cycle.setStatus(CycleStatus.COMPLETED);

        cycleHistoryRepository.save(cycle);

        return Optional.of(cycle);
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
