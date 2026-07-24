package backend.prediction.service;

import backend.prediction.dto.CyclePredictionDTO;
import backend.prediction.engine.PredictionEngine;
import backend.service.CycleHistoryService;
import backend.service.CycleService;
import backend.model.CycleData;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class PredictionService {

    private final CycleService cycleService;
    private final CycleHistoryService cycleHistoryService;

    public PredictionService(CycleService cycleService, CycleHistoryService cycleHistoryService) {
        this.cycleService = cycleService;
        this.cycleHistoryService = cycleHistoryService;
    }

    private LocalDate findReferenceStartDate(CycleData cycleData) {
        return LocalDate.parse(cycleData.getLastPeriod());
    }

    private int calculatePredictedCycleLength(CycleData cycleData) {
        return cycleData.getCycleLength();
    }

    private int calculatePredictedPeriodLength(CycleData cycleData) {
        return cycleData.getPeriodLength();
    }

    private LocalDate calculateNextPredictedStartDate(LocalDate referenceDate, LocalDate today, int predictedCycleLength) {
        return PredictionEngine.calculateNextPeriod(
            referenceDate,
            today,
            predictedCycleLength
        );
    }


    private LocalDate calculatePredictedEndDate(LocalDate predictedStartDate, int predictedPeriodLength) {
        return predictedStartDate.plusDays(predictedPeriodLength - 1);
    }

    public CyclePredictionDTO generateNextPrediction() {

        CycleData cycleData = cycleService.getCycleData();

        int predictedCycleLength =
        calculatePredictedCycleLength(cycleData);

        int predictedPeriodLength =
                calculatePredictedPeriodLength(cycleData);

        LocalDate referenceDate =
                findReferenceStartDate(cycleData);

        LocalDate today = LocalDate.now();

        LocalDate predictedStartDate =
            calculateNextPredictedStartDate(
                    referenceDate,
                    today,
                    predictedCycleLength);

        LocalDate predictedEndDate =
            calculatePredictedEndDate(
                    predictedStartDate,
                    predictedPeriodLength);

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
