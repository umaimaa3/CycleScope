package backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class CalendarEvent {

    // Primary key for uniquely identifying each calendar event in the database
    // The database automatically generates the ID when a new event is inserted
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Stores the date associated with the calendar event
    private String eventDate;

    // Stores the text/content displayed for the calendar event
    private String eventText;

    // Required by JPA to create an entity when retrieving records from the database
    public CalendarEvent() {}

    // Convenience constructor for creating a new calendar event with its initial data
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
