package backend.controller;

import backend.model.CycleData;
import backend.service.CycleService;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/cycle")
public class CycleController {

    private final CycleService cycleService;
    public CycleController(CycleService cycleService) {
        this.cycleService = cycleService;
    }

    @GetMapping
    public CycleData getCycleData() {
        return cycleService.getCycleData();
    }

    @PostMapping
    public CycleData saveCycleData(@RequestBody CycleData cycleData) {
        return cycleService.saveCycleData(cycleData);
    }
}
