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
                .map(cycleHistoryService::updateCycleStatus)
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

    @GetMapping("/current")
    public CycleHistory getCurrentRelevantCycle() {
        return cycleHistoryService.getCurrentRelevantCycle()
                .orElse(null);
    }

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

