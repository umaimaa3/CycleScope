package backend.controller;

import backend.model.CycleHistory;
import backend.dto.ConfirmPeriodEndRequest;
import backend.dto.ConfirmPeriodStartRequest;
import backend.service.CycleHistoryService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

@RestController

// All endpoints begin with /api/cycle-history
@RequestMapping("/api/cycle-history")
public class CycleHistoryController {

    // Service responsible for cycle-history business logic
    private final CycleHistoryService cycleHistoryService;

    // Spring injects CycleHistoryService through the constructor
    public CycleHistoryController(CycleHistoryService cycleHistoryService) {
        this.cycleHistoryService = cycleHistoryService;
    }

    // GET /api/cycle-history
    // Returns all stored cycle-history records
    @GetMapping
    public List<CycleHistory> getAllCycles() {
        return cycleHistoryService.getAllCycles();
    }

    // GET /api/cycle-history/{id}
    // Retrieves a specific cycle and updates its lifecycle status first
    @GetMapping("/{id}")
    public CycleHistory getCycleById(@PathVariable Long id) {
        return cycleHistoryService.getCycleById(id)
                .map(cycleHistoryService::updateCycleStatus)
                .orElse(null);
    }

    // POST /api/cycle-history
    // Creates a new cycle-history record
    @PostMapping
    public CycleHistory createCycle(@RequestBody CycleHistory cycleHistory) {
        return cycleHistoryService.saveCycleHistory(cycleHistory);
    }

    // GET /api/cycle-history/latest
    // Returns the cycle with the highest cycle number
    @GetMapping("/latest")
    public CycleHistory getLatestCycle() {
        return cycleHistoryService.getLatestCycle()
                .orElse(null);
    }

    // GET /api/cycle-history/current
    // Determines which cycle is relevant to today's date
    @GetMapping("/current")
    public CycleHistory getCurrentRelevantCycle() {
        return cycleHistoryService.getCurrentRelevantCycle()
                .orElse(null);
    }

    // POST /api/cycle-history/{id}/confirm-start
    // Receives the date the user confirms as their actual period start
    @PostMapping("/{id}/confirm-start")
    public ResponseEntity<CycleHistory> confirmPeriodStart(
            @PathVariable Long id,
            @RequestBody ConfirmPeriodStartRequest request
    ) {

        return cycleHistoryService
                .confirmPeriodStart(id, request.actualStartDate())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/cycle-history/{id}/confirm-end
    // Receives the date the user confirms as their actual period end
    @PostMapping("/{id}/confirm-end")
    public ResponseEntity<CycleHistory> confirmPeriodEnd(
            @PathVariable Long id,
            @RequestBody ConfirmPeriodEndRequest request
    ) {

        return cycleHistoryService
                .confirmPeriodEnd(id, request.actualEndDate())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
}

