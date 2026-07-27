package backend.service;

import backend.model.CycleData;
import backend.model.CycleHistory;
import backend.model.CycleStatus;
import backend.repository.CycleHistoryRepository;
import org.springframework.stereotype.Service;


import java.util.Optional;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Comparator;

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

    public Optional<CycleHistory> getNextCycle(CycleHistory currentCycle) {

        return getAllCycles()
                .stream()
                .filter(cycle ->
                        cycle.getCycleNumber() > currentCycle.getCycleNumber()
                )
                .findFirst();
    }

    private int getNextCycleNumber() {
        return getLatestCycle()
                .map(cycle -> cycle.getCycleNumber() + 1)
                .orElse(1);
    }

    // TODO: REMOVE
    public Optional<CycleHistory> getCurrentCycle() {

        Optional<CycleHistory> currentCycle = getLatestCycle();

        if (currentCycle.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(updateCycleStatus(currentCycle.get()));
    }

    public Optional<CycleHistory> getCurrentRelevantCycle() {

        LocalDate today = LocalDate.now();

        List<CycleHistory> cycles = getAllCycles();

        cycles.sort(
                Comparator.comparing(CycleHistory::getPredictedStartDate)
        );

        for (int i = 0; i < cycles.size(); i++) {

            CycleHistory current = cycles.get(i);

            LocalDate currentStart =
                    current.getActualStartDate() != null
                            ? current.getActualStartDate()
                            : current.getPredictedStartDate();

            LocalDate nextStart = null;

            if (i + 1 < cycles.size()) {

                CycleHistory next = cycles.get(i + 1);

                nextStart =
                        next.getActualStartDate() != null
                                ? next.getActualStartDate()
                                : next.getPredictedStartDate();
            }

            boolean afterCurrentStart =
                    !today.isBefore(currentStart);

            boolean beforeNextStart =
                    nextStart == null || today.isBefore(nextStart);

            if (afterCurrentStart && beforeNextStart) {
                return Optional.of(updateCycleStatus(current));
            }
        }

        return Optional.empty();
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

    public CycleStatus determineStatus(CycleHistory cycle) {

        LocalDate today = LocalDate.now();

        // Cycle has not started yet
        if (today.isBefore(cycle.getPredictedStartDate())) {
            return CycleStatus.PREDICTED;
        }

        // User has entered the cycle but has not confirmed the start
        if (cycle.getActualStartDate() == null) {
            return CycleStatus.WAITING_FOR_START_CONFIRMATION;
        }

        // User confirmed the start, but not the end
        if (cycle.getActualEndDate() == null) {

            if (today.isBefore(cycle.getPredictedEndDate())) {
                return CycleStatus.ACTIVE;
            }

            return CycleStatus.WAITING_FOR_END_CONFIRMATION;
        }

        // Both start and end have been confirmed.
        // TODO: Later we'll determine when this should become COMPLETED.
        return CycleStatus.ACTIVE;
    }

    public CycleHistory updateCycleStatus(CycleHistory cycle) {

        CycleStatus newStatus = determineStatus(cycle);

        if (cycle.getStatus() != newStatus) {
            cycle.setStatus(newStatus);
            cycleHistoryRepository.save(cycle);
        }

        return cycle;
    }

    private boolean predictionAlreadyExists(LocalDate predictedStartDate) {

        return getLatestCycle()
                .map(cycle -> cycle.getPredictedStartDate().equals(predictedStartDate))
                .orElse(false);
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

        cycle.setStatus(determineStatus(cycle)); 

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

        cycle.setStatus(determineStatus(cycle));

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

        cycleHistory.setStatus(determineStatus(cycleHistory));
        
        saveCycleHistory(cycleHistory);
    }

}
