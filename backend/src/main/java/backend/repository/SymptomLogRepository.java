package backend.repository;

import backend.model.SymptomLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Repository for accessing SymptomLog records in the database
public interface SymptomLogRepository extends JpaRepository<SymptomLog, Long> {

    // Spring Data JPA derives the query from the method name:
    // Finds all symptoms recorded on the specified date
    List<SymptomLog> findByLogDate(String logDate);
}
