package backend.prediction.dto;

import java.time.LocalDate;

public class CyclePredictionDTO {

    private int cycleDay;
    private String phase;
    private int cycleLength;
    private LocalDate nextPeriodDate;

    public CyclePredictionDTO(int cycleDay, String phase, int cycleLength, LocalDate nextPeriodDate) {
        this.cycleDay = cycleDay;
        this.phase = phase;
        this.cycleLength = cycleLength;
        this.nextPeriodDate = nextPeriodDate;
    }

    public int getCycleDay() { return cycleDay; }
    public String getPhase() { return phase; }
    public int getCycleLength() { return cycleLength; }
    public LocalDate getNextPeriodDate() { return nextPeriodDate; }
}
