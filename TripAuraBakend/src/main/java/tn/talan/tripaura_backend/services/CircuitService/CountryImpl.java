package tn.talan.tripaura_backend.services.CircuitService;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.talan.tripaura_backend.entities.Circuit.Country;
import tn.talan.tripaura_backend.repositories.Circuit.CountryRepo;

import java.util.List;

@Service
@AllArgsConstructor
public class CountryImpl implements  CountryService{
    private CountryRepo countryRepo;
    @Override
    public Country addCountry(Country country) {
        return countryRepo.save(country);
    }

    @Override
    public List<Country> getAllCountry() {
        return countryRepo.findAll();
    }
}
