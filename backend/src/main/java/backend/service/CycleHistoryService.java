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

// Service layer responsible for cycle-history business logic and persistence
@Service
public class CycleHistoryService {

    // Repository used to retrieve and persist cycle-history records
    private final CycleHistoryRepository cycleHistoryRepository;

    // Constructor injection provides the repository dependency
    public CycleHistoryService(CycleHistoryRepository cycleHistoryRepository) {
        this.cycleHistoryRepository = cycleHistoryRepository;
    }

    // Saves a cycle-history record to the database
    public CycleHistory saveCycleHistory(CycleHistory cycleHistory) {
        cycleHistory.setStatus(determineStatus(cycleHistory));
        return cycleHistoryRepository.save(cycleHistory);
    }

    // Retrieves all cycle-history records
    public List<CycleHistory> getAllCycles() {
        return cycleHistoryRepository.findAll();
    }

    // Retrieves a cycle by its database ID
    // Optional handles the case where no matching cycle exists
    public Optional<CycleHistory> getCycleById(Long id) {
        return cycleHistoryRepository.findById(id);
    }

    // Retrieves the cycle with the highest cycle number
    // Used to identify the latest cycle in the user's history
    public Optional<CycleHistory> getLatestCycle() {
        return cycleHistoryRepository.findTopByOrderByCycleNumberDesc();
    }

    // Finds the first cycle with a higher cycle number than the current cycle
    public Optional<CycleHistory> getNextCycle(CycleHistory currentCycle) {

        return getAllCycles()
                .stream()
                .filter(cycle ->
                        cycle.getCycleNumber() > currentCycle.getCycleNumber()
                )
                .findFirst();
    }

    // Generates the next sequential cycle number
    // Starts at 1 when no cycle history exists yet
    private int getNextCycleNumber() {
        return getLatestCycle()
                .map(cycle -> cycle.getCycleNumber() + 1)
                .orElse(1);
    }

    // Finds the cycle with the highest cycle number below the current cycle
    // Used to calculate the previous cycle's actual length once the current
    // cycle's actual start date has been confirmed
    public Optional<CycleHistory> getPreviousCycle(CycleHistory currentCycle) {

        return getAllCycles()
                .stream()
                .filter(cycle ->
                        cycle.getCycleNumber() < currentCycle.getCycleNumber()
                )
                .max(Comparator.comparing(CycleHistory::getCycleNumber));
    }

    // Calculates the actual cycle length of the previous cycle once
    // the current cycle's actual start date is known
    private void calculatePreviousCycleLength(CycleHistory currentCycle) {

        if (currentCycle.getActualStartDate() == null) {
            return;
        }

        Optional<CycleHistory> previousCycle =
                getPreviousCycle(currentCycle);

        if (previousCycle.isEmpty()) {
            return;
        }

        CycleHistory previous = previousCycle.get();

        // The previous cycle's actual length cannot be calculated
        // without both actual start dates
        if (previous.getActualStartDate() == null) {
            return;
        }

        long cycleLength = ChronoUnit.DAYS.between(
                previous.getActualStartDate(),
                currentCycle.getActualStartDate()
        );

        previous.setActualCycleLength((int) cycleLength);

        cycleHistoryRepository.save(previous);
    }

    // Calculates the actual period length of a cycle once 
    // the actual period start and end dates are confirmed
    private void calculateActualPeriodLength(CycleHistory cycle) {
        if (cycle.getActualStartDate() == null ||
            cycle.getActualEndDate() == null) {
            return;
        }

        long periodLength = ChronoUnit.DAYS.between(
            cycle.getActualStartDate(),
            cycle.getActualEndDate()
        ) + 1;

        cycle.setActualPeriodLength((int) periodLength);
    }

    // TODO: REMOVE
    // Older method for identifying the current cycle
    // getCurrentRelevantCycle() is the newer approach
    public Optional<CycleHistory> getCurrentCycle() {

        Optional<CycleHistory> currentCycle = getLatestCycle();

        if (currentCycle.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(updateCycleStatus(currentCycle.get()));
    }

    // Finds the cycle whose start-date interval contains today
    // Actual user-confirmed dates take priority over predicted dates
    public Optional<CycleHistory> getCurrentRelevantCycle() {

        LocalDate today = LocalDate.now();

        List<CycleHistory> cycles = getAllCycles();

        // Sort cycles chronologically so each cycle can be compared
        // with the start of the following cycle
        cycles.sort(
                Comparator.comparing(CycleHistory::getPredictedStartDate)
        );

        for (int i = 0; i < cycles.size(); i++) {

            CycleHistory current = cycles.get(i);

            // Use the actual start when available because user-confirmed
            // observations take priority over predictions
            LocalDate currentStart =
                    current.getActualStartDate() != null
                            ? current.getActualStartDate()
                            : current.getPredictedStartDate();

            LocalDate nextStart = null;

            if (i + 1 < cycles.size()) {

                CycleHistory next = cycles.get(i + 1);

                // Likewise, use the next cycle's actual start when available
                nextStart =
                        next.getActualStartDate() != null
                                ? next.getActualStartDate()
                                : next.getPredictedStartDate();
            }

            // Today must be on or after the current cycle's start
            boolean afterCurrentStart =
                    !today.isBefore(currentStart);

            // Today must be before the next cycle's start
            // The final cycle has no next start, so it remains valid afterward
            boolean beforeNextStart =
                    nextStart == null || today.isBefore(nextStart);

            // If today falls between the current and next cycle starts,
            // this is the cycle currently relevant to the user
            if (afterCurrentStart && beforeNextStart) {
                return Optional.of(updateCycleStatus(current));
            }
        }

        return Optional.empty();
    }

    // Retrieves a cycle only if it exists and currently has the expected status.
    // This prevents confirmation operations from being performed on
    // cycles in the wrong lifecycle state.
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

    // Determines the cycle's current lifecycle state from today's date
    // and the information currently confirmed for the cycle
    public CycleStatus determineStatus(CycleHistory cycle) {

        LocalDate today = LocalDate.now();

        // Cycle has not started yet according to its predicted start date
        if (today.isBefore(cycle.getPredictedStartDate())) {
            return CycleStatus.PREDICTED;
        }

        // The predicted start has been reached, but the user has not
        // confirmed the actual start date yet
        if (cycle.getActualStartDate() == null) {
            return CycleStatus.WAITING_FOR_START_CONFIRMATION;
        }

        // The user confirmed the start, but has not confirmed the end
        if (cycle.getActualEndDate() == null) {

            // The predicted end has not been reached yet
            if (today.isBefore(cycle.getPredictedEndDate())) {
                return CycleStatus.ACTIVE;
            }

            // The predicted end has passed, so the application
            // should wait for the user to confirm the actual end
            return CycleStatus.WAITING_FOR_END_CONFIRMATION;
        }

        // Both start and end have been confirmed
        // TODO: Later we'll determine when this should become COMPLETED.
        return CycleStatus.ACTIVE;
    }

    // Recalculates and persists the cycle's lifecycle status if it has changed
    public CycleHistory updateCycleStatus(CycleHistory cycle) {

        CycleStatus newStatus = determineStatus(cycle);

        // Avoid an unnecessary database write when the status is unchanged
        if (cycle.getStatus() != newStatus) {
            cycle.setStatus(newStatus);
            cycleHistoryRepository.save(cycle);
        }

        return cycle;
    }

    // Prevents the same predicted cycle from being stored more than once
    private boolean predictionAlreadyExists(LocalDate predictedStartDate) {

        return getLatestCycle()
                .map(cycle -> cycle.getPredictedStartDate().equals(predictedStartDate))
                .orElse(false);
    }

    // Confirms the user's actual period start and records the prediction error
    public Optional<CycleHistory> confirmPeriodStart(Long id, LocalDate actualStartDate) {

        // Only allow this operation while the cycle is waiting
        // for start confirmation
        Optional<CycleHistory> optionalCycle =
                getCycleInStatus(
                        id,
                        CycleStatus.WAITING_FOR_START_CONFIRMATION
                );

        if (optionalCycle.isEmpty()) {
            return Optional.empty();
        }

        CycleHistory cycle = optionalCycle.get();

        // Store the user-confirmed start date separately from the prediction
        cycle.setActualStartDate(actualStartDate);

        // Calculate how many days the actual start differed from the prediction
        long error = ChronoUnit.DAYS.between(
                cycle.getPredictedStartDate(),
                actualStartDate
        );

        cycle.setPredictionErrorDays((int) error);

        // Calculate the previous cycle's actual length now that
        // this cycle's actual start date is known
        calculatePreviousCycleLength(cycle);

        // Recalculate the lifecycle state after receiving the confirmation
        cycle.setStatus(determineStatus(cycle)); 

        cycleHistoryRepository.save(cycle);

        return Optional.of(cycle);
    }

    // Confirms the user's actual period end and calculates the observed
    // period length
    public Optional<CycleHistory> confirmPeriodEnd(Long id, LocalDate actualEndDate) {

        // Only allow this operation while the cycle is waiting
        // for end confirmation
        Optional<CycleHistory> optionalCycle =
                getCycleInStatus(
                        id,
                        CycleStatus.WAITING_FOR_END_CONFIRMATION
                );

        if (optionalCycle.isEmpty()) {
            return Optional.empty();
        }

        CycleHistory cycle = optionalCycle.get();

        // Store the user-confirmed end date separately from the prediction
        cycle.setActualEndDate(actualEndDate);

        // Calculate the number of calendar days from start through end
        // using a helper function
        calculateActualPeriodLength(cycle);

        // Recalculate the lifecycle state after receiving the confirmation
        cycle.setStatus(determineStatus(cycle));

        cycleHistoryRepository.save(cycle);

        return Optional.of(cycle);
    }

    // Creates and stores a new predicted cycle unless that prediction
    // has already been recorded
    public void recordPredictedCycle(
        LocalDate predictedStartDate,
        LocalDate predictedEndDate,
        int predictedCycleLength,
        int predictedPeriodLength,
        Integer confidence) {
        
        // Prevent duplicate predictions from being inserted
        if (predictionAlreadyExists(predictedStartDate)) {
            return;
        }

        // Create a new historical record containing the prediction
        CycleHistory cycleHistory = new CycleHistory(
                getNextCycleNumber(),
                predictedStartDate,
                predictedEndDate,
                predictedCycleLength,
                predictedPeriodLength,
                confidence
        );

        // Determine its initial lifecycle state based on the current date
        cycleHistory.setStatus(determineStatus(cycleHistory));
        
        // Persist the new prediction
        saveCycleHistory(cycleHistory);
    }

}
