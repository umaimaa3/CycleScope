package backend.prediction.service;

import backend.prediction.dto.CyclePredictionDTO;
import backend.prediction.engine.PredictionEngine;
import backend.service.CycleService;
import backend.model.CycleData;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class PredictionService {

    private final CycleService cycleService;

    public PredictionService(CycleService cycleService) {
        this.cycleService = cycleService;
    }

    public CyclePredictionDTO calculatePrediction() {

        CycleData cycleData = cycleService.getCycleData();

        int cycleLength = cycleData.getCycleLength();
        int periodLength = cycleData.getPeriodLength();

        LocalDate lastPeriod = LocalDate.parse(cycleData.getLastPeriod());
        LocalDate today = LocalDate.now();

        long diffDays = ChronoUnit.DAYS.between(lastPeriod, today);

        // Internal 0-based value
        int cycleDayIndex = PredictionEngine.normalizeCycleDay((int) diffDays, cycleLength);
        // User-facing 1-based value
        int cycleDay = cycleDayIndex + 1;

        String phase = PredictionEngine.calculatePhase(cycleDayIndex, periodLength, cycleLength);

        LocalDate nextPeriod = PredictionEngine.calculateNextPeriod(lastPeriod, today, cycleLength);

        return new CyclePredictionDTO(
                cycleDayIndex,
                cycleDay,
                phase,
                cycleLength,
                periodLength,
                nextPeriod
        );
    }
}
