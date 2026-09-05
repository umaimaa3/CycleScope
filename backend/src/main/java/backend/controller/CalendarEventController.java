package backend.controller;

import backend.model.CalendarEvent;
import backend.service.CalendarEventService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController

// All endpoints in this controller begin with /api/calendar-events
@RequestMapping("/api/calendar-events")
public class CalendarEventController {

    // Service responsible for the calendar event business logic
    private final CalendarEventService calendarEventService;

    // Constructor injection allows Spring to provide the service dependency
    public CalendarEventController(CalendarEventService calendarEventService) {
        this.calendarEventService = calendarEventService;
    }

    // GET /api/calendar-events
    // Returns all calendar events
    @GetMapping
    public List<CalendarEvent> getAllEvents() {
        return calendarEventService.getAllEvents();
    }

    // POST /api/calendar-events
    // Receives a CalendarEvent as JSON and asks the service to save it
    @PostMapping
    public CalendarEvent addEvent(@RequestBody CalendarEvent calendarEvent) {
        return calendarEventService.addEvent(calendarEvent);
    }

    // PUT /api/calendar-events/{id}
    // Updates the calendar event identified by the URL ID
    @PutMapping("/{id}")
    public CalendarEvent updateEvent(
            @PathVariable Long id,
            @RequestBody CalendarEvent calendarEvent
    ) {
        return calendarEventService.updateEvent(id, calendarEvent);
    }

    // DELETE /api/calendar-events/{id}
    // Deletes the event identified by the URL ID
    @DeleteMapping("/{id}")
    public void deleteEvent(@PathVariable Long id) {
        calendarEventService.deleteEvent(id);
    }

}
