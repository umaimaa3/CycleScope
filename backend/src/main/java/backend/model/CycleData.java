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
    private String lastPeriod;

    public CycleData() {}

    public CycleData(int cycleLength, String lastPeriod) {
        this.cycleLength = cycleLength;
        this.lastPeriod = lastPeriod;
    }

    public int getCycleLength() {
        return cycleLength;
    }

    public String getLastPeriod() {
        return lastPeriod;
    }

    public void setCycleLength(int cycleLength) {
        this.cycleLength = cycleLength;
    }

    public void setLastPeriod(String lastPeriod) {
        this.lastPeriod = lastPeriod;
    }
}
