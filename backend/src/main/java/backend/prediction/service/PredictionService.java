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

@Service
public class PredictionService {

    private final CycleService cycleService;
    private final CycleHistoryService cycleHistoryService;

    public PredictionService(CycleService cycleService, CycleHistoryService cycleHistoryService) {
        this.cycleService = cycleService;
        this.cycleHistoryService = cycleHistoryService;
    }

    private LocalDate findReferenceStartDate(CycleData cycleData) {

        Optional<CycleHistory> latestCycle = cycleHistoryService.getLatestCycle();

        if (latestCycle.isPresent()) {

            CycleHistory cycle = latestCycle.get();

            if (cycle.getActualStartDate() != null) {
                return cycle.getActualStartDate();
            }

            return cycle.getPredictedStartDate();
        }

        return LocalDate.parse(cycleData.getLastPeriod());
    }

    private int calculatePredictedCycleLength(CycleData cycleData) {
        return cycleData.getCycleLength();
    }

    private int calculatePredictedPeriodLength(CycleData cycleData) {
        return cycleData.getPeriodLength();
    }

    private LocalDate calculateNextPredictedStartDate(LocalDate referenceDate, int predictedCycleLength) {
        return PredictionEngine.calculateNextPeriod(
            referenceDate,
            predictedCycleLength
        );
    }

    private LocalDate calculatePredictedEndDate(LocalDate predictedStartDate, int predictedPeriodLength) {
        return predictedStartDate.plusDays(predictedPeriodLength - 1);
    }

    private boolean needsNewPrediction() {

        Optional<CycleHistory> latestCycle =
                cycleHistoryService.getLatestCycle();

        if (latestCycle.isEmpty()) {
            return true;
        }

        LocalDate today = LocalDate.now();

        return !latestCycle.get()
                .getPredictedEndDate()
                .isAfter(today);
    }


    private CyclePredictionDTO convertCycleHistoryToDTO(CycleHistory cycle, CycleHistory nextCycle) {

        LocalDate cycleStartDate =
                cycle.getActualStartDate() != null
                        ? cycle.getActualStartDate()
                        : cycle.getPredictedStartDate();

        LocalDate today = LocalDate.now();

        long diffDays =
                ChronoUnit.DAYS.between(cycleStartDate, today);

        int cycleDayIndex =
                PredictionEngine.normalizeCycleDay(
                        (int) diffDays,
                        cycle.getPredictedCycleLength()
                );

        int cycleDay = cycleDayIndex + 1;

        String phase =
                PredictionEngine.calculatePhase(
                        cycleDayIndex,
                        cycle.getPredictedPeriodLength(),
                        cycle.getPredictedCycleLength()
                );

        return new CyclePredictionDTO(
                cycleDayIndex,
                cycleDay,
                phase,
                cycle.getPredictedCycleLength(),
                cycle.getPredictedPeriodLength(),
                nextCycle.getPredictedStartDate()
        );
    }

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

    public CyclePredictionDTO generateNextPrediction() {

        if (!needsNewPrediction()) {
            return getCurrentPredictionDTO();
        }

        CycleData cycleData = cycleService.getCycleData();

        int predictedCycleLength =
        calculatePredictedCycleLength(cycleData);

        int predictedPeriodLength =
                calculatePredictedPeriodLength(cycleData);

        LocalDate referenceDate =
                findReferenceStartDate(cycleData);

        LocalDate predictedStartDate =
            calculateNextPredictedStartDate(
                    referenceDate,
                    predictedCycleLength);

        LocalDate predictedEndDate =
            calculatePredictedEndDate(
                    predictedStartDate,
                    predictedPeriodLength);
        
        LocalDate today = LocalDate.now();
        long diffDays = ChronoUnit.DAYS.between(referenceDate, today);

        // Internal 0-based value
        int cycleDayIndex = PredictionEngine.normalizeCycleDay((int) diffDays, predictedCycleLength);
        // User-facing 1-based value
        int cycleDay = cycleDayIndex + 1;

        String phase = PredictionEngine.calculatePhase(cycleDayIndex, predictedPeriodLength, predictedCycleLength);

        // for cycleHistory

        cycleHistoryService.recordPredictedCycle(
                predictedStartDate,
                predictedEndDate,
                predictedCycleLength,
                predictedPeriodLength,
                70
        );

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
