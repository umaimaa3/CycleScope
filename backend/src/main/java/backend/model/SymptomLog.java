package backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class SymptomLog {

    // Primary key used to uniquely identify each symptom entry
    // The database automatically generates the ID when a record is created
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Date on which the symptom was recorded
    private String logDate;

    // Type/name of the symptom recorded by the user
    private String symptom;

    // Optional intensity of the symptom, represented on a 1–5 scale
    private Integer intensity;

    public SymptomLog() {}

    // Convenience constructor for creating a symptom log with its initial data
    public SymptomLog(String logDate, String symptom, Integer intensity) {
        this.logDate = logDate;
        this.symptom = symptom;
        this.intensity = intensity;
    }

    public Long getId() {
        return id;
    }

    public String getLogDate() {
        return logDate;
    }

    public String getSymptom() {
        return symptom;
    }

    public Integer getIntensity() {
        return intensity;
    }

    public void setLogDate(String logDate) {
        this.logDate = logDate;
    }

    public void setSymptom(String symptom) {
        this.symptom = symptom;
    }

    public void setIntensity(Integer intensity) {
        this.intensity = intensity;
    }
    
}
