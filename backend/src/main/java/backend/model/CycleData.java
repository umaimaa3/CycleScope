package backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity 
public class CycleData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    
    private int cycleLength;
    private int periodLength;
    private String lastPeriod;

    public CycleData() {}

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
