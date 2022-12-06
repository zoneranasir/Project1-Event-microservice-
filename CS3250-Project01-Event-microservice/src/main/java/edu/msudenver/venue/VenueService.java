package edu.msudenver.venue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class VenueService {
    @Autowired
    private VenueRepository venueRepository;
    @PersistenceContext
    protected EntityManager entityManager;

    public VenueService() {
    }

    public List<Venue> getVenues() {
        return this.venueRepository.findAll();
    }

    public Venue getVenue(Integer entityId) {
        try {
            return (Venue)this.venueRepository.findById(entityId).get();
        } catch (IllegalArgumentException | NoSuchElementException var3) {
            var3.printStackTrace();
            return null;
        }
    }

    @Transactional
    public Venue saveVenue(Venue venue) {
        venue = (Venue)this.venueRepository.saveAndFlush(venue);
        this.entityManager.refresh(venue);
        return venue;
    }

    public boolean deleteVenue(Integer venueId) {
        try {
            if (this.venueRepository.existsById(venueId)) {
                this.venueRepository.deleteById(venueId);
                return true;
            }
        } catch (IllegalArgumentException var3) {
        }

        return false;
    }
}
