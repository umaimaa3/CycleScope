package backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class CalendarEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String eventDate;
    private String eventText;

    public CalendarEvent() {}

    public CalendarEvent(String eventDate, String eventText) {
        this.eventDate = eventDate;
        this.eventText = eventText;
    }

    public Long getId() {
        return id;
    }

    public String getEventDate() {
        return eventDate;
    }

    public String getEventText() {
        return eventText;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public void setEventText(String eventText) {
        this.eventText = eventText;
    }
    
}
