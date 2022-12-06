

package edu.msudenver.event;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
        path = {"/events"}
)
public class EventController {
    @Autowired
    private EventService eventService;

    public EventController() {
    }

    @GetMapping(
            produces = {"application/json"}
    )
    public ResponseEntity<List<Event>> getEvents() {
        return ResponseEntity.ok(this.eventService.getEvents());
    }

    @GetMapping(
            path = {"/{eventId}"},
            produces = {"application/json"}
    )
    public ResponseEntity<Event> getEvent(@PathVariable Integer eventId) {
        Event event = this.eventService.getEvent(eventId);
        return new ResponseEntity(event, event == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }

    @PostMapping(
            consumes = {"application/json"},
            produces = {"application/json"}
    )
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        try {
            return new ResponseEntity(this.eventService.saveEvent(event), HttpStatus.CREATED);
        } catch (Exception var3) {
            var3.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(
            path = {"/{eventId}"},
            consumes = {"application/json"},
            produces = {"application/json"}
    )
    public ResponseEntity<Event> updateEvent(@PathVariable Integer eventId, @RequestBody Event updatedEvent) {
        Event retrievedEvent = this.eventService.getEvent(eventId);
        if (retrievedEvent != null) {
            retrievedEvent.setTitle(updatedEvent.getTitle());

            try {
                return ResponseEntity.ok(this.eventService.saveEvent(retrievedEvent));
            } catch (Exception var5) {
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(
            path = {"/{eventId}"}
    )
    public ResponseEntity<Void> deleteEvent(@PathVariable Integer eventId) {
        return new ResponseEntity(this.eventService.deleteEvent(eventId) ? HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND);
    }
}
