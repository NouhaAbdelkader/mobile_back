package tn.talan.tripaura_backend.repositories.flightRepo;
import tn.talan.tripaura_backend.entities.Flights.Flight;
import tn.talan.tripaura_backend.entities.Flights.FlightClass;
import tn.talan.tripaura_backend.entities.Flights.FlightType;

import java.util.Date;
import java.util.List;

public interface FlightRepoCustom {
    List<Flight> findBySearchCriteria(String departure, String destination, Date departureDate, Date returnDate, FlightClass flightClass, FlightType flightType);

}
