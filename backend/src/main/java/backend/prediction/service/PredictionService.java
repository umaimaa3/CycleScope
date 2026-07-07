package backend.prediction.service;

import backend.prediction.dto.CyclePredictionDTO;
import backend.prediction.engine.PredictionEngine;
import backend.service.CycleService;
import backend.model.CycleData;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class PredictionService {

    private final CycleService cycleService;

    public PredictionService(CycleService cycleService) {
        this.cycleService = cycleService;
    }

    public CyclePredictionDTO calculatePrediction() {

        CycleData cycleData = cycleService.getCycleData();

        int cycleLength = cycleData.getCycleLength();
        LocalDate lastPeriod = LocalDate.parse(cycleData.getLastPeriod());

        LocalDate today = LocalDate.now();

        long diffDays = java.time.temporal.ChronoUnit.DAYS.between(lastPeriod, today);

        System.out.println("Backend today: " + today);
        System.out.println("Last period: " + lastPeriod);
        System.out.println("Diff days: " + diffDays);

        int cycleDay = PredictionEngine.normalizeCycleDay((int) diffDays, cycleLength);

        String phase = PredictionEngine.calculatePhase(cycleDay, cycleLength);

        LocalDate nextPeriod = PredictionEngine.calculateNextPeriod(lastPeriod, today, cycleLength);

        return new CyclePredictionDTO(
                cycleDay,
                phase,
                cycleLength,
                nextPeriod
        );
    }
}
