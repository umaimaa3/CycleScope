package backend.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated; 

@Entity 
public class CycleHistory {

    // Primary key used to uniquely identify each historical cycle record
    // The database automatically generates the ID when a record is created
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Represents where this cycle currently is in the prediction/confirmation
    // lifecycle. The initial state is PREDICTED.
    //
    // EnumType.STRING stores the enum name (eg. "PREDICTED") in the database
    // rather than its numeric position, making the stored value more readable
    // and safer if the enum order changes
    @Enumerated(EnumType.STRING)
    private CycleStatus status = CycleStatus.PREDICTED;

    // Identifies the cycle's position in the user's historical sequence
    private int cycleNumber;

    // Values predicted by CycleScope for when the user's period will start and end
    // These remain separate from actual dates so predictions can later be evaluated
    private LocalDate predictedStartDate;
    private LocalDate actualStartDate;

    // Dates supplied or confirmed by the user once the actual cycle events are known
    // These are kept separate from predictions because predictions must never
    // overwrite user-confirmed observations
    private LocalDate predictedEndDate;
    private LocalDate actualEndDate;

    // Predicted length of the entire cycle and the predicted length of the period
    private int predictedCycleLength;
    private Integer actualCycleLength;

    // Actual cycle and period lengths once enough user-confirmed information exists
    // Integer is used so these fields can be null while the values are still unknown
    private int predictedPeriodLength;
    private Integer actualPeriodLength;

    // Difference between the predicted and actual cycle timing, calculated once
    // actual information becomes available
    private Integer predictionErrorDays;

    // Confidence associated with the prediction
    private Integer confidence;
    
    // Timestamps used to track when the historical record was created and last updated
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CycleHistory() {
    }

    // Creates a new historical record containing the initial prediction
    // Actual values remain empty until the user provides observations
    public CycleHistory(int cycleNumber, LocalDate predictedStartDate, LocalDate predictedEndDate,
        int predictedCycleLength, int predictedPeriodLength, Integer confidence) {

        this.cycleNumber = cycleNumber;
        this.predictedStartDate = predictedStartDate;
        this.predictedEndDate = predictedEndDate;
        this.predictedCycleLength = predictedCycleLength;
        this.predictedPeriodLength = predictedPeriodLength;
        this.confidence = confidence;
        this.status = CycleStatus.PREDICTED;

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

    public CycleStatus getStatus() {
        return status;
    }

    public void setStatus(CycleStatus status){
        this.status = status;
    }

}
