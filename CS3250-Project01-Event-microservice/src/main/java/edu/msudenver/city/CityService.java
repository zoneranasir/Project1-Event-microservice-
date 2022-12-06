

package edu.msudenver.city;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CityService {
    @Autowired
    private CityRepository cityRepository;
    @PersistenceContext
    public EntityManager entityManager;

    public CityService() {
    }

    public List<City> getCities() {
        return this.cityRepository.findAll();
    }

    public City getCity(String countryCode, String postalCode) {
        CityId cityId = new CityId(countryCode, postalCode);

        try {
            return (City)this.cityRepository.findById(cityId).get();
        } catch (IllegalArgumentException | NoSuchElementException var5) {
            return null;
        }
    }

    @Transactional
    public City saveCity(City city) {
        city = (City)this.cityRepository.saveAndFlush(city);
        this.entityManager.refresh(city);
        return city;
    }

    public boolean deleteCity(String countryCode, String postalCode) {
        CityId cityId = new CityId(countryCode, postalCode);

        try {
            if (this.cityRepository.existsById(cityId)) {
                this.cityRepository.deleteById(cityId);
                return true;
            }
        } catch (IllegalArgumentException var5) {
        }

        return false;
    }
}
