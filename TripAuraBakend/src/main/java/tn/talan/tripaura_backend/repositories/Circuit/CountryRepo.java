package tn.talan.tripaura_backend.repositories.Circuit;

import org.springframework.data.mongodb.repository.MongoRepository;
import tn.talan.tripaura_backend.entities.Circuit.Country;

public interface CountryRepo extends MongoRepository<Country, String> {
    Country findCountryById(String id);
    Country findCountryByNom(String id);
}
