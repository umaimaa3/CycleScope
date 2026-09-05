package backend.repository;

import backend.model.CycleHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// Repository for accessing CycleHistory records in the database
public interface CycleHistoryRepository extends JpaRepository<CycleHistory, Long>{

    // Finds the CycleHistory record with the highest cycleNumber
    // Optional is used because the database may contain no cycle records
    Optional<CycleHistory> findTopByOrderByCycleNumberDesc();
}
