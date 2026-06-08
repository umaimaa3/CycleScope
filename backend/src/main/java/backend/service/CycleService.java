package backend.service;

import backend.model.CycleData;
import org.springframework.stereotype.Service;

@Service
public class CycleService {

    private CycleData savedCycleData =
        new CycleData(28, "2026-06-01");

    public CycleData getCycleData() {
        return savedCycleData;
    }

    public CycleData saveCycleData(CycleData cycleData) {
        savedCycleData = cycleData;
        return savedCycleData;
    }
}
