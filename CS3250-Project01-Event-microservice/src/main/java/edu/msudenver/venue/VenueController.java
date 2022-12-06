//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package edu.msudenver.venue;

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
        path = {"/venues"}
)
public class VenueController {
    @Autowired
    private VenueService venueService;

    public VenueController() {
    }

    @GetMapping(
            produces = {"application/json"}
    )
    public ResponseEntity<List<Venue>> getVenues() {
        return ResponseEntity.ok(this.venueService.getVenues());
    }

    @GetMapping(
            path = {"/{venueId}"},
            produces = {"application/json"}
    )
    public ResponseEntity<Venue> getVenue(@PathVariable Integer venueId) {
        Venue venue = this.venueService.getVenue(venueId);
        return new ResponseEntity(venue, venue == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }

    @PostMapping(
            consumes = {"application/json"},
            produces = {"application/json"}
    )
    public ResponseEntity<Venue> createVenue(@RequestBody Venue venue) {
        try {
            return new ResponseEntity(this.venueService.saveVenue(venue), HttpStatus.CREATED);
        } catch (Exception var3) {
            var3.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(
            path = {"/{venueId}"},
            consumes = {"application/json"},
            produces = {"application/json"}
    )
    public ResponseEntity<Venue> updateVenue(@PathVariable Integer venueId, @RequestBody Venue updatedVenue) {
        Venue retrievedVenue = this.venueService.getVenue(venueId);
        if (retrievedVenue != null) {
            retrievedVenue.setName(updatedVenue.getName());

            try {
                return ResponseEntity.ok(this.venueService.saveVenue(retrievedVenue));
            } catch (Exception var5) {
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(
            path = {"/{venueId}"}
    )
    public ResponseEntity<Void> deleteVenue(@PathVariable Integer venueId) {
        return new ResponseEntity(this.venueService.deleteVenue(venueId) ? HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND);
    }
}
