package backend.prediction.service;

import backend.prediction.dto.CyclePredictionDTO;
import backend.prediction.engine.PredictionEngine;
import backend.service.CycleHistoryService;
import backend.service.CycleService;
import backend.model.CycleData;
import backend.model.CycleHistory;

import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

// Service responsible for orchestrating the prediction workflow:
// It gathers the required data, calls the prediction engine,
// stores predictions, and prepares the response for the frontend.
@Service
public class PredictionService {

    // Provides access to the user's cycle configuration
    private final CycleService cycleService;

    // Provides access to historical cycle data
    private final CycleHistoryService cycleHistoryService;

    // Constructor injection provides the services this prediction workflow depends on
    public PredictionService(CycleService cycleService, CycleHistoryService cycleHistoryService) {
        this.cycleService = cycleService;
        this.cycleHistoryService = cycleHistoryService;
    }

    // Determines which period start date should be used as the
    // reference point for generating the next prediction
    private LocalDate findReferenceStartDate(CycleData cycleData) {

        Optional<CycleHistory> latestCycle = cycleHistoryService.getLatestCycle();

        // Prefer the latest cycle's actual start when available
        if (latestCycle.isPresent()) {

            CycleHistory cycle = latestCycle.get();

            if (cycle.getActualStartDate() != null) {
                return cycle.getActualStartDate();
            }

            // Otherwise fall back to the latest predicted start
            return cycle.getPredictedStartDate();
        }

        // If there is no cycle history yet, use the onboarding
        // last-period date from CycleData
        return LocalDate.parse(cycleData.getLastPeriod());
    }

    // Gets the cycle length used for the prediction
    // Currently this comes directly from CycleData
    private int calculatePredictedCycleLength(CycleData cycleData) {
        return cycleData.getCycleLength();
    }

    // Gets the period length used for the prediction
    // Currently this comes directly from CycleData
    private int calculatePredictedPeriodLength(CycleData cycleData) {
        return cycleData.getPeriodLength();
    }

    // Calculates the predicted start date of the next period
    // using the prediction engine's date calculation
    private LocalDate calculateNextPredictedStartDate(LocalDate referenceDate, int predictedCycleLength) {
        return PredictionEngine.calculateNextPeriod(
            referenceDate,
            predictedCycleLength
        );
    }

    // Calculates the predicted end date based on the predicted
    // start date and expected period length
    private LocalDate calculatePredictedEndDate(LocalDate predictedStartDate, int predictedPeriodLength) {
        return predictedStartDate.plusDays(predictedPeriodLength - 1);
    }

    // Determines whether a new future prediction needs to be generated
    private boolean needsNewPrediction() {

        Optional<CycleHistory> latestCycle =
                cycleHistoryService.getLatestCycle();

        // No history means there is no prediction yet
        if (latestCycle.isEmpty()) {
            return true;
        }

        LocalDate today = LocalDate.now();

        // Generate a new prediction once the latest predicted cycle
        // has reached or passed its predicted end date
        return !latestCycle.get()
                .getPredictedEndDate()
                .isAfter(today);
    }

    // Converts stored cycle-history data into the DTO returned to the frontend
    private CyclePredictionDTO convertCycleHistoryToDTO(CycleHistory cycle, CycleHistory nextCycle) {

        // Actual start takes priority over the predicted start
        // when calculating the current cycle day
        LocalDate cycleStartDate =
                cycle.getActualStartDate() != null
                        ? cycle.getActualStartDate()
                        : cycle.getPredictedStartDate();

        LocalDate today = LocalDate.now();

        // Calculate the number of days since the cycle began
        long diffDays =
                ChronoUnit.DAYS.between(cycleStartDate, today);

        // Normalize the result into the valid cycle-day range
        int cycleDayIndex =
                PredictionEngine.normalizeCycleDay(
                        (int) diffDays,
                        cycle.getPredictedCycleLength()
                );
        
        // Convert the internal 0-based day into a user-facing 1-based day
        int cycleDay = cycleDayIndex + 1;
        
        // Determine the current cycle phase using the prediction engine
        String phase =
                PredictionEngine.calculatePhase(
                        cycleDayIndex,
                        cycle.getPredictedPeriodLength(),
                        cycle.getPredictedCycleLength()
                );
        
        // Package the calculated values into the response DTO
        return new CyclePredictionDTO(
                cycleDayIndex,
                cycleDay,
                phase,
                cycle.getPredictedCycleLength(),
                cycle.getPredictedPeriodLength(),
                nextCycle.getPredictedStartDate()
        );
    }

    // Retrieves the current relevant cycle and the cycle after it,
    // then converts them into the prediction response
    private CyclePredictionDTO getCurrentPredictionDTO() {

        CycleHistory currentCycle =
                cycleHistoryService.getCurrentRelevantCycle()
                        .orElseThrow();

        CycleHistory nextCycle =
                cycleHistoryService.getNextCycle(currentCycle)
                        .orElseThrow();

        return convertCycleHistoryToDTO(
                currentCycle,
                nextCycle
        );
    }

    // Main method responsible for generating or retrieving
    // the prediction presented to the frontend
    public CyclePredictionDTO generateNextPrediction() {

        // If the existing prediction is still relevant,
        // reuse it instead of generating another one
        if (!needsNewPrediction()) {
            return getCurrentPredictionDTO();
        }

        // Retrieve the user's cycle configuration
        CycleData cycleData = cycleService.getCycleData();

        // Determine the values used for the next prediction
        int predictedCycleLength =
        calculatePredictedCycleLength(cycleData);

        // Determine the date from which the next cycle should be predicted
        int predictedPeriodLength =
                calculatePredictedPeriodLength(cycleData);

        // Determine the date from which the next cycle should be predicted
        LocalDate referenceDate =
                findReferenceStartDate(cycleData);

        // Calculate the next predicted period start date
        LocalDate predictedStartDate =
            calculateNextPredictedStartDate(
                    referenceDate,
                    predictedCycleLength);
        
        // Calculate the predicted period end date
        LocalDate predictedEndDate =
            calculatePredictedEndDate(
                    predictedStartDate,
                    predictedPeriodLength);
        
        LocalDate today = LocalDate.now();

        // Determine the number of days since the reference cycle began
        long diffDays = ChronoUnit.DAYS.between(referenceDate, today);

        // Internal 0-based cycle day
        int cycleDayIndex = PredictionEngine.normalizeCycleDay((int) diffDays, predictedCycleLength);

        // User-facing 1-based cycle day
        int cycleDay = cycleDayIndex + 1;

        // Determine the current cycle phase
        String phase = PredictionEngine.calculatePhase(cycleDayIndex, predictedPeriodLength, predictedCycleLength);

        // Store the prediction in CycleHistory so it can be retained
        // for future lifecycle tracking and prediction comparison
        cycleHistoryService.recordPredictedCycle(
                predictedStartDate,
                predictedEndDate,
                predictedCycleLength,
                predictedPeriodLength,
                70
        );

        // Return the calculated prediction to the frontend
        return new CyclePredictionDTO(
                cycleDayIndex,
                cycleDay,
                phase,
                predictedCycleLength,
                predictedPeriodLength,
                predictedStartDate
        );
    }
}
