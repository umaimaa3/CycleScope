package backend.service;

import backend.model.CalendarEvent;
import backend.repository.CalendarEventRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalendarEventService {
    
    private final CalendarEventRepository calendarEventRepository;

    public CalendarEventService(CalendarEventRepository calendarEventRepository) {
        this.calendarEventRepository = calendarEventRepository;
    }

    public List<CalendarEvent> getAllEvents() {
        return calendarEventRepository.findAll();
    }

    public CalendarEvent addEvent(CalendarEvent calendarEvent) {
        return calendarEventRepository.save(calendarEvent);
    }

    public CalendarEvent updateEvent(Long id, CalendarEvent updatedEvent) {
        CalendarEvent existingEvent = calendarEventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        existingEvent.setEventText(updatedEvent.getEventText());

        return calendarEventRepository.save(existingEvent);
    }

    public void deleteEvent(Long id) {
        calendarEventRepository.deleteById(id);
    }
    
}
