package backend.service;

import backend.model.CycleData;
import backend.repository.CycleRepository;
import java.time.LocalDate;
import org.springframework.stereotype.Service;

// Service layer responsible for managing the user's cycle configuration
@Service
public class CycleService {

    // Repository used to access CycleData records in the database
    private final CycleRepository cycleRepository;

    // Constructor injection provides the repository dependency
    public CycleService(CycleRepository cycleRepository) {
        this.cycleRepository = cycleRepository;
    }

    // Retrieves the user's stored cycle configuration
    // If none exists, returns default values for initial use
    public CycleData getCycleData() {
        return cycleRepository.findAll()
                .stream()
                .findFirst()
                .orElse(new CycleData(28, 5, LocalDate.now().toString()));
    }

    // Validates and saves the user's cycle configuration
    public CycleData saveCycleData(CycleData cycleData) {

        // Replace invalid cycle lengths with the default value
        if (cycleData.getCycleLength() <= 0) {
            cycleData.setCycleLength(28);
        }

        // Replace invalid period lengths with the default value
        if (cycleData.getPeriodLength() <= 0) {
            cycleData.setPeriodLength(5);
        }

        // Retrieve the existing configuration if one exists
        // Otherwise, create a new CycleData object
        CycleData existingCycleData = cycleRepository.findAll()
            .stream()
            .findFirst()
            .orElse(new CycleData());

        // Update the stored configuration with the new values
        existingCycleData.setCycleLength(cycleData.getCycleLength());
        existingCycleData.setPeriodLength(cycleData.getPeriodLength());
        existingCycleData.setLastPeriod(cycleData.getLastPeriod());

        // Save the configuration to the database
        return cycleRepository.save(existingCycleData); 
    }
}
