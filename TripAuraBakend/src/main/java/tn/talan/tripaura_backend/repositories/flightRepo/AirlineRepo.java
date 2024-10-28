package tn.talan.tripaura_backend.repositories.flightRepo;

import org.springframework.data.mongodb.repository.MongoRepository;
import tn.talan.tripaura_backend.entities.Flights.Airline;

public interface AirlineRepo extends MongoRepository<Airline, String>   {
    public Boolean existsAirlineByCode(String code) ;
    public Boolean existsAirlineByLogoUrl(String logoUrl) ;
    public  Boolean existsAirlineByName(String name);

}
