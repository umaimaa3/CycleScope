package backend.prediction.engine;

import java.time.LocalDate;

public class PredictionEngine {

    public static int normalizeCycleDay(int diffDays, int cycleLength) {
        return ((diffDays % cycleLength) + cycleLength) % cycleLength;
    }

    public static String calculatePhase(int cycleDay, int periodLength, int cycleLength) {

        int ovulationDay = Math.max(cycleLength - 14, 10);

        if (cycleDay < periodLength) {
            return "Menstrual";
        } else if (cycleDay < ovulationDay) {
            return "Follicular";
        } else if (cycleDay == ovulationDay) {
            return "Ovulation";
        } else {
            return "Luteal";
        }
    }

    public static LocalDate calculateNextPeriod(LocalDate lastPeriod, LocalDate today, int cycleLength) {

        LocalDate next = lastPeriod;

        while (!next.isAfter(today)) {
            next = next.plusDays(cycleLength);
        }

        return next;
    }
}
