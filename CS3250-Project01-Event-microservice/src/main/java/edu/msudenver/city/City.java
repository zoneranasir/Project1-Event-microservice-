package edu.msudenver.city;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import edu.msudenver.country.Country;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "cities")
@IdClass(CityId.class)
public class City {
    @Id
    @Column(name = "postal_code")
    private String postalCode;
    @Id
    @ManyToOne
    @JoinColumn(
            name = "country_code",
            referencedColumnName = "country_code",
            insertable = false,
            updatable = false
    )
    private Country country;
    @Column(name = "country_code")
    @JsonProperty(access = Access.WRITE_ONLY)
    private String countryCode;
    @Column(name = "name")
    private String name;

    public City(String postalCode, Country country, String countryCode, String name) {
        this.postalCode = postalCode;
        this.country = country;
        this.countryCode = countryCode;
        this.name = name;
    }

    public City() {}

    public String getPostalCode() {
        return this.postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Country getCountry() {
        return this.country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getCountryCode() {
        return this.countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
