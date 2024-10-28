package tn.talan.tripaura_backend.services.CircuitService;

import org.springframework.web.multipart.MultipartFile;
import tn.talan.tripaura_backend.entities.Circuit.Circuit;
import tn.talan.tripaura_backend.entities.Circuit.Programme;
import tn.talan.tripaura_backend.entities.Flights.Flight;

import java.util.Date;
import java.util.List;

public interface CircuitService {
    public Circuit addCircuitWithProgrammes(Circuit circuit,List<MultipartFile> galleryImages);
    public void deleteCircuit(String id);
    public Circuit getCircuitById(String id);
    public List<Circuit> getAllCircuits() ;
    public List<Programme> findProgByCircuit(String acc) ;
    public Circuit updateCircuitWithProgrammes(Circuit circuit);
    public void deleteProgramme(String id) ;
    public List<Flight>getFlightByDepartureAndDestination(String departure, String destination);
    public List<Circuit> getCircuitBuUser(String id);
    public List<Circuit> filterCircuits(String countryName, String city, String theme, String type, Date startDate, Date endDate);
}
