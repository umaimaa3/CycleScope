package backend.repository;

import backend.model.CycleData;
import org.springframework.data.jpa.repository.JpaRepository;

// Repository for accessing the user's CycleData configuration
public interface CycleRepository extends JpaRepository<CycleData, Long> {
}
