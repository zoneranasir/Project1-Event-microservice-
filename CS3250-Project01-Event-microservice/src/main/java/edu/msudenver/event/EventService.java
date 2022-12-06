
package edu.msudenver.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;
    @PersistenceContext
    protected EntityManager entityManager;

    public EventService() {
    }

    public List<Event> getEvents() {
        return this.eventRepository.findAll();
    }

    public Event getEvent(Integer entityId) {
        try {
            return (Event)this.eventRepository.findById(entityId).get();
        } catch (IllegalArgumentException | NoSuchElementException var3) {
            var3.printStackTrace();
            return null;
        }
    }

    @Transactional
    public Event saveEvent(Event event) {
        event = (Event)this.eventRepository.saveAndFlush(event);
        this.entityManager.refresh(event);
        return event;
    }

    public boolean deleteEvent(Integer eventId) {
        try {
            if (this.eventRepository.existsById(eventId)) {
                this.eventRepository.deleteById(eventId);
                return true;
            }
        } catch (IllegalArgumentException var3) {
        }

        return false;
    }
}
