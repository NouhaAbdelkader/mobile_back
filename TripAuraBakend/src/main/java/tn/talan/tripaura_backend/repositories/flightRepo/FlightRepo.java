package tn.talan.tripaura_backend.repositories.flightRepo;

import org.springframework.data.mongodb.repository.MongoRepository;
import tn.talan.tripaura_backend.entities.Flights.Flight;
import tn.talan.tripaura_backend.entities.Flights.FlightClass;

import java.util.Date;
import java.util.List;


public interface FlightRepo  extends MongoRepository<Flight, String> , FlightRepoCustom{
    public Boolean existsFlightByflightNumber(String flightNumber);
    List<Flight> findByFlightNumberIn(List<String> noms);
    Flight findFlightByFlightNumber(String flightNumber);
    List<Flight> findFlightByDepartureAndDestination(String departure, String destination);
    List<Flight> findFlightsByDepartureAndDestinationAndDepartureDateAndReturnDate(
            String departure, String destination, Date departureDate, Date returnDate);
    // Custom query to find flights by class and sort by price
    List<Flight> findByFlightClassesContainingOrderByEconomyPriceAsc(FlightClass flightClass);

    List<Flight> findByFlightClassesContainingOrderByBusinessPriceAsc(FlightClass flightClass);

    List<Flight> findByFlightClassesContainingOrderByPremiumEconomyPriceAsc(FlightClass flightClass);

    List<Flight> findByFlightClassesContainingOrderByFirstClassPriceAsc(FlightClass flightClass);

}
