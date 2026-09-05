package backend.prediction.engine;

import java.time.LocalDate;

// Contains the core calculation logic used by CycleScope's prediction system
// This class does not access the database or manage application state
public class PredictionEngine {

    // Converts a day difference into a valid 0-based cycle-day index.
    // The modulo operation makes the cycle wrap back to day 0
    // after reaching the end of the cycle.
    public static int normalizeCycleDay(int diffDays, int cycleLength) {
        return ((diffDays % cycleLength) + cycleLength) % cycleLength;
    }

    // Determines the menstrual cycle phase from the 0-based cycle day
    public static String calculatePhase(int cycleDay, int periodLength, int cycleLength) {

        // Estimate ovulation as 14 days before the expected next period
        // while enforcing a minimum cycle-day position of 10
        int ovulationDay = Math.max(cycleLength - 14, 10);

        // Days before the end of the user's period are classified as menstrual
        if (cycleDay < periodLength) {
            return "Menstrual";
        
        // Days after menstruation but before ovulation are follicular
        } else if (cycleDay < ovulationDay) {
            return "Follicular";

        // The estimated ovulation day is classified separately
        } else if (cycleDay == ovulationDay) {
            return "Ovulation";
        
        // Remaining days are classified as luteal
        } else {
            return "Luteal";
        }
    }

    // Calculates the predicted start date of the next period
    // by adding the expected cycle length to the reference start date
    public static LocalDate calculateNextPeriod(LocalDate referenceStartDate, int cycleLength) {
        return referenceStartDate.plusDays(cycleLength);
    }
}
