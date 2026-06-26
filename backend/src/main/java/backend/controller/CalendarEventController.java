package backend.controller;

import backend.model.CalendarEvent;
import backend.service.CalendarEventService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/calendar-events")
public class CalendarEventController {

    private final CalendarEventService calendarEventService;

    public CalendarEventController(CalendarEventService calendarEventService) {
        this.calendarEventService = calendarEventService;
    }

    @GetMapping
    public List<CalendarEvent> getAllEvents() {
        return calendarEventService.getAllEvents();
    }

    @PostMapping
    public CalendarEvent addEvent(@RequestBody CalendarEvent calendarEvent) {
        return calendarEventService.addEvent(calendarEvent);
    }

    @PutMapping("/{id}")
    public CalendarEvent updateEvent(
            @PathVariable Long id,
            @RequestBody CalendarEvent calendarEvent
    ) {
        return calendarEventService.updateEvent(id, calendarEvent);
    }

    @DeleteMapping("/{id}")
    public void deleteEvent(@PathVariable Long id) {
        calendarEventService.deleteEvent(id);
    }

}
