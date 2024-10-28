package tn.talan.tripaura_backend.services.CircuitService;

import tn.talan.tripaura_backend.entities.Circuit.City;

import java.util.List;

public interface CityService {
    City addCity(City city, String countryId);
    public List<City> getCityByCountry(String id);
}
