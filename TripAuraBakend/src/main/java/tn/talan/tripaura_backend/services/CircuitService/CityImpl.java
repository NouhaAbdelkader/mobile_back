package tn.talan.tripaura_backend.services.CircuitService;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.talan.tripaura_backend.entities.Circuit.City;
import tn.talan.tripaura_backend.entities.Circuit.Country;
import tn.talan.tripaura_backend.repositories.Circuit.CityRepo;
import tn.talan.tripaura_backend.repositories.Circuit.CountryRepo;

import java.util.List;

@Service
@AllArgsConstructor
public class CityImpl implements CityService{
    private CityRepo cityRepo;
    private CountryRepo countryRepo;
    @Override
    public City addCity(City city, String countryId) {
        if (city == null) {
            throw new IllegalArgumentException("City cannot be null");
        }
        if (countryId == null || countryId.isEmpty()) {
            throw new IllegalArgumentException("Country ID cannot be null or empty");
        }

        Country country = countryRepo.findCountryById(countryId);
        if (country == null) {
            throw new IllegalArgumentException("Country not found for ID: " + countryId);
        }

        city.setCountry(country);
        return cityRepo.save(city);
    }
    public List<City> getCityByCountry(String id) {
        Country c = countryRepo.findCountryById(id);
        return  cityRepo.findCitiesByCountry(c);
    }

}
