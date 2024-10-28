package tn.talan.tripaura_backend.services.FlightService;

import org.springframework.web.multipart.MultipartFile;
import tn.talan.tripaura_backend.entities.Flights.Airline;
import java.util.List;

public interface IAirlineService {
    public Airline addAirline(Airline airline, MultipartFile file);
    public List<Airline> getAirlines();
    public void deleteAirline(String AirlineId);
    public Airline updateAirline(Airline airline, MultipartFile file);
    List<Airline> searchAirlines(String name , String code , String country);

}
