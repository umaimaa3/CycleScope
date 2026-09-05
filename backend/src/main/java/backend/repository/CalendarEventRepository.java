package backend.repository;

import backend.model.CalendarEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Repository for accessing CalendarEvent records in the database
public interface CalendarEventRepository extends JpaRepository<CalendarEvent, Long> {

    // Spring Data JPA derives the query from the method name:
    // finds all calendar events whose eventDate matches the given date
    List<CalendarEvent> findByEventDate(String eventDate);
}
