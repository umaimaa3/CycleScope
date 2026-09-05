package backend.service;

import backend.model.CalendarEvent;
import backend.repository.CalendarEventRepository;
import org.springframework.stereotype.Service;

import java.util.List;

// Service layer responsible for calendar event operations
@Service
public class CalendarEventService {
    
    // Repository used to access CalendarEvent records in the database
    private final CalendarEventRepository calendarEventRepository;

    // Constructor injection gives the service the repository it depends on
    public CalendarEventService(CalendarEventRepository calendarEventRepository) {
        this.calendarEventRepository = calendarEventRepository;
    }

    // Retrieves all calendar events from the database
    public List<CalendarEvent> getAllEvents() {
        return calendarEventRepository.findAll();
    }

    // Saves a new calendar event to the database
    public CalendarEvent addEvent(CalendarEvent calendarEvent) {
        return calendarEventRepository.save(calendarEvent);
    }

    // Updates an existing calendar event
    public CalendarEvent updateEvent(Long id, CalendarEvent updatedEvent) {

        // Find the existing event or fail if no event exists with this ID
        CalendarEvent existingEvent = calendarEventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        // Update only the fields that are allowed to change
        existingEvent.setEventText(updatedEvent.getEventText());

        // Save the modified entity back to the database
        return calendarEventRepository.save(existingEvent);
    }

    // Deletes the calendar event with the specified ID
    public void deleteEvent(Long id) {
        calendarEventRepository.deleteById(id);
    }
    
}
