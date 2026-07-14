package backend.prediction.dto;

import java.time.LocalDate;

public class CyclePredictionDTO {

    private int cycleDayIndex; //0-based for internal
    private int cycleDay;      //1-based for users
    private String phase;
    private int cycleLength;
    private int periodLength;
    private LocalDate nextPeriodDate;

    public CyclePredictionDTO() {
    }
    
    public CyclePredictionDTO(int cycleDayIndex, int cycleDay, String phase, int cycleLength, 
        int periodLength, LocalDate nextPeriodDate) {

        this.cycleDayIndex = cycleDayIndex;
        this.cycleDay = cycleDay;
        this.phase = phase;
        this.cycleLength = cycleLength;
        this.periodLength = periodLength;
        this.nextPeriodDate = nextPeriodDate;
    }

    public int getCycleDayIndex() { return cycleDayIndex; }
    public int getCycleDay() { return cycleDay; }
    public String getPhase() { return phase; }
    public int getCycleLength() { return cycleLength; }
    public int getPeriodLength() { return periodLength; }
    public LocalDate getNextPeriodDate() { return nextPeriodDate; }
}
