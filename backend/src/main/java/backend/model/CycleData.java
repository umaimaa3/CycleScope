package backend.model;

public class CycleData {
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
