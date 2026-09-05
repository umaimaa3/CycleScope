package backend.controller;

import backend.model.CycleData;
import backend.service.CycleService;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController

// All endpoints begin with /api/cycle
@RequestMapping("/api/cycle")
public class CycleController {

    // Service responsible for managing cycle configuration
    private final CycleService cycleService;

    // Spring injects CycleService through the constructor
    public CycleController(CycleService cycleService) {
        this.cycleService = cycleService;
    }

    // GET /api/cycle
    // Retrieves the user's cycle configuration
    @GetMapping
    public CycleData getCycleData() {
        return cycleService.getCycleData();
    }

    // POST /api/cycle
    // Receives cycle configuration from the frontend and saves it
    @PostMapping
    public CycleData saveCycleData(@RequestBody CycleData cycleData) {
        return cycleService.saveCycleData(cycleData);
    }
}
