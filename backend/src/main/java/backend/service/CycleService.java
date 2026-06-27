package backend.service;

import backend.model.CycleData;
import backend.repository.CycleRepository;
import org.springframework.stereotype.Service;

@Service
public class CycleService {

    private final CycleRepository cycleRepository;

    public CycleService(CycleRepository cycleRepository) {
        this.cycleRepository = cycleRepository;
    }

    public CycleData getCycleData() {
        return cycleRepository.findAll()
                .stream()
                .findFirst()
                .orElse(new CycleData(28, "2026-06-01"));
    }

    public CycleData saveCycleData(CycleData cycleData) {
        CycleData existingCycleData = cycleRepository.findAll()
            .stream()
            .findFirst()
            .orElse(new CycleData());

        existingCycleData.setCycleLength(cycleData.getCycleLength());
        existingCycleData.setLastPeriod(cycleData.getLastPeriod());

        return cycleRepository.save(existingCycleData); 
    }
}
