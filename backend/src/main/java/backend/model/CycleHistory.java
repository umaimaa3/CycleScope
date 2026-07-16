package backend.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity 
public class CycleHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    private int cycleNumber;

    private LocalDate predictedStartDate;
    private LocalDate actualStartDate;

    private LocalDate predictedEndDate;
    private LocalDate actualEndDate;

    private int predictedCycleLength;
    private Integer actualCycleLength;

    private int predictedPeriodLength;
    private Integer actualPeriodLength;

    private Integer predictionErrorDays;
    private Integer confidence;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CycleHistory() {
    }

    public CycleHistory(int cycleNumber, LocalDate predictedStartDate, LocalDate predictedEndDate,
        int predictedCycleLength, int predictedPeriodLength, Integer confidence) {

        this.cycleNumber = cycleNumber;
        this.predictedStartDate = predictedStartDate;
        this.predictedEndDate = predictedEndDate;
        this.predictedCycleLength = predictedCycleLength;
        this.predictedPeriodLength = predictedPeriodLength;
        this.confidence = confidence;

        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public int getCycleNumber() {
        return cycleNumber;
    }

    public void setCycleNumber(int cycleNumber) {
        this.cycleNumber = cycleNumber;
    }

    public LocalDate getPredictedStartDate() {
        return predictedStartDate;
    }

    public void setPredictedStartDate(LocalDate predictedStartDate) {
        this.predictedStartDate = predictedStartDate;
    }

    public LocalDate getActualStartDate() {
        return actualStartDate;
    }

    public void setActualStartDate(LocalDate actualStartDate) {
        this.actualStartDate = actualStartDate;
    }

    public LocalDate getPredictedEndDate() {
        return predictedEndDate;
    }

    public void setPredictedEndDate(LocalDate predictedEndDate) {
        this.predictedEndDate = predictedEndDate;
    }

    public LocalDate getActualEndDate() {
        return actualEndDate;
    }

    public void setActualEndDate(LocalDate actualEndDate) {
        this.actualEndDate = actualEndDate;
    }

    public int getPredictedCycleLength() {
        return predictedCycleLength;
    }

    public void setPredictedCycleLength(int predictedCycleLength) {
        this.predictedCycleLength = predictedCycleLength;
    }

    public Integer getActualCycleLength() {
        return actualCycleLength;
    }

    public void setActualCycleLength(Integer actualCycleLength) {
        this.actualCycleLength = actualCycleLength;
    }

    public int getPredictedPeriodLength() {
        return predictedPeriodLength;
    }

    public void setPredictedPeriodLength(int predictedPeriodLength) {
        this.predictedPeriodLength = predictedPeriodLength;
    }

    public Integer getActualPeriodLength() {
        return actualPeriodLength;
    }

    public void setActualPeriodLength(Integer actualPeriodLength) {
        this.actualPeriodLength = actualPeriodLength;
    }

    public Integer getPredictionErrorDays() {
        return predictionErrorDays;
    }

    public void setPredictionErrorDays(Integer predictionErrorDays) {
        this.predictionErrorDays = predictionErrorDays;
    }

    public Integer getConfidence() {
        return confidence;
    }

    public void setConfidence(Integer confidence) {
        this.confidence = confidence;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

}
