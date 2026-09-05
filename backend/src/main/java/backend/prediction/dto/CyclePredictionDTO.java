package backend.prediction.dto;

import java.time.LocalDate;

// Response DTO containing the calculated cycle information sent to the frontend
public class CyclePredictionDTO {

    // 0-based cycle day used internally by the prediction logic
    private int cycleDayIndex; 

    // 1-based cycle day displayed to the user
    private int cycleDay;     
    
    // Current cycle phase
    private String phase;

    // Cycle length used for this prediction
    private int cycleLength;

    // Predicted date when the next period will begin
    private int periodLength;
    private LocalDate nextPeriodDate;

    public CyclePredictionDTO() {
    }
    
    // Creates a prediction response containing all calculated cycle information
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
