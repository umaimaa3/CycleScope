package backend.controller;

import backend.model.CycleHistory;
import backend.service.CycleHistoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cycle-history")
public class CycleHistoryController {

    private final CycleHistoryService cycleHistoryService;

    public CycleHistoryController(CycleHistoryService cycleHistoryService) {
        this.cycleHistoryService = cycleHistoryService;
    }

    @GetMapping
    public List<CycleHistory> getAllCycles() {
        return cycleHistoryService.getAllCycles();
    }

    @GetMapping("/{id}")
    public CycleHistory getCycleById(@PathVariable Long id) {
        return cycleHistoryService.getCycleById(id)
                .orElse(null);
    }

    @PostMapping
    public CycleHistory createCycle(@RequestBody CycleHistory cycleHistory) {
        return cycleHistoryService.saveCycleHistory(cycleHistory);
    }

    @GetMapping("/latest")
    public CycleHistory getLatestCycle() {
        return cycleHistoryService.getLatestCycle()
                .orElse(null);
    }
    
}
