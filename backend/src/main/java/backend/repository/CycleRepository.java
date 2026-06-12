package backend.repository;

import backend.model.CycleData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CycleRepository extends JpaRepository<CycleData, Long> {
}
