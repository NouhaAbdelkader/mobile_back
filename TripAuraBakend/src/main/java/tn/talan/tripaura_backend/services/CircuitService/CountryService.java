package tn.talan.tripaura_backend.services.CircuitService;

import tn.talan.tripaura_backend.entities.Circuit.Country;

import java.util.List;

public interface CountryService {
   public  Country addCountry(Country country);
   public List<Country> getAllCountry( );
   public void deleteCountry(String id);
}
