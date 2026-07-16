package backend.service;

import backend.model.CycleData;
import backend.repository.CycleRepository;
import java.time.LocalDate;
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
                .orElse(new CycleData(28, 5, LocalDate.now().toString()));
    }

    public CycleData saveCycleData(CycleData cycleData) {

        if (cycleData.getCycleLength() <= 0) {
            cycleData.setCycleLength(28);
        }

        if (cycleData.getPeriodLength() <= 0) {
            cycleData.setPeriodLength(5);
        }

        CycleData existingCycleData = cycleRepository.findAll()
            .stream()
            .findFirst()
            .orElse(new CycleData());

        existingCycleData.setCycleLength(cycleData.getCycleLength());
        existingCycleData.setPeriodLength(cycleData.getPeriodLength());
        existingCycleData.setLastPeriod(cycleData.getLastPeriod());

        return cycleRepository.save(existingCycleData); 
    }
}
