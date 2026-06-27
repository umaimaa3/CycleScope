package backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class SymptomLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String logDate;
    private String symptom;

    public SymptomLog() {}

    public SymptomLog(String logDate, String symptom) {
        this.logDate = logDate;
        this.symptom = symptom;
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

    public void setLogDate(String logDate) {
        this.logDate = logDate;
    }

    public void setSymptom(String symptom) {
        this.symptom = symptom;
    }
    
}
