package backend.controller;

import backend.model.SymptomLog;
import backend.service.SymptomLogService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/symptoms")
public class SymptomLogController {
    private final SymptomLogService symptomLogService;

    public SymptomLogController(SymptomLogService symptomLogService) {
        this.symptomLogService = symptomLogService;
    }

    @GetMapping
    public List<SymptomLog> getAllSymptoms() {
        return symptomLogService.getAllSymptoms();
    }

    @PostMapping
    public SymptomLog addSymptom(@RequestBody SymptomLog symptomLog) {
        return symptomLogService.addSymptom(symptomLog);
    }

    @DeleteMapping("/{id}")
    public void deleteSymptom(@PathVariable Long id) {
        symptomLogService.deleteSymptom(id);
    }
}
