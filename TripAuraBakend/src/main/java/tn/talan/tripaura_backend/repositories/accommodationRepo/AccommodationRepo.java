package tn.talan.tripaura_backend.repositories.accommodationRepo;

import org.springframework.data.mongodb.repository.MongoRepository;
import tn.talan.tripaura_backend.entities.Circuit.City;
import tn.talan.tripaura_backend.entities.Circuit.Country;
import tn.talan.tripaura_backend.entities.accommodation.Accommodation;
import tn.talan.tripaura_backend.entities.accommodation.AccommodationType;

import java.util.List;

public interface AccommodationRepo extends MongoRepository<Accommodation, String>{
    public List<Accommodation> findAccommodationByAccomType(AccommodationType accommodationType);
    public List<Accommodation> findAccommodationByAccomTypeAndCity(AccommodationType accommodationType, City city);
    public List<Accommodation> findAccommodationByCity(City city);
    public List<Accommodation> findAccommodationByCountry(Country country);
    public List<Accommodation> findAccommodationByFull(Boolean full);
    public Accommodation findAccommodationById(String id);
    public List<Accommodation> findAccommodationByRateNumber(int n) ;
    public List<Accommodation> findAccommodationByOrderByDateDesc () ;
    List<Accommodation> findByNameIn(List<String> noms);
    Accommodation findAccommodationByName(String name);


}
