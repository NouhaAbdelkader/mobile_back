package tn.talan.tripaura_backend.repositories.Circuit;

import org.springframework.data.mongodb.repository.MongoRepository;
import tn.talan.tripaura_backend.entities.Circuit.City;
import tn.talan.tripaura_backend.entities.Circuit.Country;

import java.util.List;

public interface CityRepo extends  MongoRepository<City, String> {
    City findCitiesById(String id);
    List<City> findCitiesByCountry(Country country) ;
    City findCitiesByNom(String nom);
}
