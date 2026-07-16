package backend.repository;

import backend.model.CycleHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CycleHistoryRepository extends JpaRepository<CycleHistory, Long>{
    Optional<CycleHistory> findTopByOrderByCycleNumberDesc();
}
