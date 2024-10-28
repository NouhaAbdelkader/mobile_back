package tn.talan.tripaura_backend.services.FlightService;

import tn.talan.tripaura_backend.entities.Flights.Flight;
import tn.talan.tripaura_backend.entities.Flights.FlightClass;
import tn.talan.tripaura_backend.entities.Flights.FlightType;

import java.util.Date;
import java.util.List;

public interface IflightService {
    public Flight addFlight(Flight flight);
    List<Flight> searchFlights(String departure, String destination, Date departureDate, Date returnDate, FlightClass flightClass, FlightType flightType);
   public List<Flight> getFlights();
    public void deleteFlight(String flightId);
    public Flight updateFlight( Flight flight) ;

    }
