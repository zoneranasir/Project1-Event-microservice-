
package edu.msudenver.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.msudenver.venue.Venue;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "events")
public class Event {
    @Column(name = "title")
    String title;
    @Id
    @Column(name = "event_id")
    @GeneratedValue(strategy = GenerationType.TABLE)
    Integer eventId;
    @Column(name = "starts")
    Date starts;
    @Column(name = "ends")
    Date ends;

    @ManyToOne
    @JoinColumn(
            name = "venue_id",
            referencedColumnName = "venue_id",
            insertable = false,
            updatable = false
    )
    private Venue venue;

    @Column(name = "venue_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer venueId;

    public Event() {}


    public String getTitle() {
        return this.title;
    }

    public Date getStarts() {
        return this.starts;
    }

    public Date getEnds() {
        return this.ends;
    }

    public Integer getEventId() {
        return this.eventId;
    }

    public Event setTitle(String title) {
        this.title = title;
        return this;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public Integer getVenueId() {
        return venueId;
    }

    public void setVenueId(Integer venueId) {
        this.venueId = venueId;
    }
}
