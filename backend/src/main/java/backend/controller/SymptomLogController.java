package backend.controller;

import backend.model.SymptomLog;
import backend.service.SymptomLogService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController

// All endpoints begin with /api/symptoms
@RequestMapping("/api/symptoms")
public class SymptomLogController {

    // Service responsible for symptom-related operations
    private final SymptomLogService symptomLogService;

    // Spring injects the service through the constructor
    public SymptomLogController(SymptomLogService symptomLogService) {
        this.symptomLogService = symptomLogService;
    }

    // GET /api/symptoms
    // Returns all recorded symptoms
    @GetMapping
    public List<SymptomLog> getAllSymptoms() {
        return symptomLogService.getAllSymptoms();
    }

    // POST /api/symptoms
    // Receives a symptom record and saves it
    @PostMapping
    public SymptomLog addSymptom(@RequestBody SymptomLog symptomLog) {
        return symptomLogService.addSymptom(symptomLog);
    }

    // DELETE /api/symptoms/{id}
    // Deletes the symptom record with the specified ID
    @DeleteMapping("/{id}")
    public void deleteSymptom(@PathVariable Long id) {
        symptomLogService.deleteSymptom(id);
    }
}
