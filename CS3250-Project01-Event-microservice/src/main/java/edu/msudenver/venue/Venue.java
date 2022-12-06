package edu.msudenver.venue;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.msudenver.city.City;

import javax.persistence.*;

@Entity
@Table(name = "venues")
public class Venue {
    @Id
    @Column(name = "venue_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer venueId;
    @Column(name = "name")
    private String name;
    @Column(name = "street_address")
    private String streetAddress;
    @Column(name = "type")
    private String type;
    @Column(name = "active")
    private Boolean active;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "postal_code", referencedColumnName = "postal_code", updatable = false, insertable = false),
            @JoinColumn(name = "country_code", referencedColumnName = "country_code", updatable = false, insertable = false)
    })
    private City city;

    @Column(name = "postal_code")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String postalCode;

    @Column(name = "country_code")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String countryCode;

    public Venue() {}

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getVenueId() {
        return this.venueId;
    }

    public void setVenueId(Integer venueId) {
        this.venueId = venueId;
    }

    public String getStreetAddress() {
        return this.streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getActive() {
        return this.active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}
