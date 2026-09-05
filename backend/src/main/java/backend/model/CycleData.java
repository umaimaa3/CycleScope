package backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity 
public class CycleData {

    // Primary key used to uniquely identify the user's cycle configuration
    // The database automatically generates the ID when a record is created
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    
    // User's configured cycle length, used for initial setup and as a
    // fallback prediction parameter when insufficient historical data exists
    private int cycleLength;

    // User's configured period length, used for initial setup and as a
    // fallback prediction parameter when insufficient historical data exists
    private int periodLength;

    // User-provided start date of their most recent period
    private String lastPeriod;

    public CycleData() {}

    // Convenience constructor for creating the initial cycle configuration
    public CycleData(int cycleLength, int periodLength, String lastPeriod) {
        this.cycleLength = cycleLength;
        this.periodLength = periodLength;
        this.lastPeriod = lastPeriod;
    }

    public int getCycleLength() {
        return cycleLength;
    }

    public int getPeriodLength() {
        return periodLength;
    }

    public String getLastPeriod() {
        return lastPeriod;
    }

    public void setCycleLength(int cycleLength) {
        this.cycleLength = cycleLength;
    }

    public void setPeriodLength(int periodLength) {
        this.periodLength = periodLength;
    }

    public void setLastPeriod(String lastPeriod) {
        this.lastPeriod = lastPeriod;
    }
}
