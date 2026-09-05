package backend.service;

import backend.model.SymptomLog;
import backend.repository.SymptomLogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

// Service layer responsible for managing symptom records
@Service
public class SymptomLogService {

    // Repository used to access SymptomLog records in the database
    private final SymptomLogRepository symptomLogRepository;

    // Constructor injection provides the repository dependency
    public SymptomLogService(SymptomLogRepository symptomLogRepository) {
        this.symptomLogRepository = symptomLogRepository;
    }

    // Retrieves all recorded symptoms
    public List<SymptomLog> getAllSymptoms() {
        return symptomLogRepository.findAll();
    }

    // Saves a new symptom record to the database
    public SymptomLog addSymptom(SymptomLog symptomLog) {
        return symptomLogRepository.save(symptomLog);
    }

    // Deletes a symptom record by its database ID
    public void deleteSymptom(Long id) {
        symptomLogRepository.deleteById(id);
    }
}
