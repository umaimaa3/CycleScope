package backend.service;

import backend.model.SymptomLog;
import backend.repository.SymptomLogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SymptomLogService {
    private final SymptomLogRepository symptomLogRepository;

    public SymptomLogService(SymptomLogRepository symptomLogRepository) {
        this.symptomLogRepository = symptomLogRepository;
    }

    public List<SymptomLog> getAllSymptoms() {
        return symptomLogRepository.findAll();
    }

    public SymptomLog addSymptom(SymptomLog symptomLog) {
        return symptomLogRepository.save(symptomLog);
    }

    public void deleteSymptom(Long id) {
        symptomLogRepository.deleteById(id);
    }
}
