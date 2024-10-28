package tn.talan.tripaura_backend.services.FlightService;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tn.talan.tripaura_backend.entities.Flights.Flight;
import tn.talan.tripaura_backend.entities.Flights.FlightClass;
import tn.talan.tripaura_backend.entities.Flights.FlightStatus;
import tn.talan.tripaura_backend.entities.Flights.FlightType;
import tn.talan.tripaura_backend.exceptions.CustomException;
import tn.talan.tripaura_backend.repositories.flightRepo.FlightRepo;
import tn.talan.tripaura_backend.services.CircuitService.CircuitGenerationService;
import tn.talan.tripaura_backend.services.FlightService.GeocodingService.ArrivalDateCalculator;
import tn.talan.tripaura_backend.services.FlightService.GeocodingService.GeocodingService;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class FlightService implements IflightService {
    private final FlightRepo flightRepo;
    private final GeocodingService geocodingService;

    private static final Logger logger = LoggerFactory.getLogger(FlightService.class);

    @Override
    public List<Flight> searchFlights(String departure, String destination, Date departureDate, Date returnDate, FlightClass flightClass, FlightType flightType) {
        return flightRepo.findBySearchCriteria(departure, destination, departureDate, returnDate, flightClass, flightType);
    }

    @Override
    public List<Flight> getFlights() {
        return flightRepo.findAll();
    }

    @Scheduled(fixedRate = 60000) // Run every minute
    public void updateFlightStatus() {
        List<Flight> flights = flightRepo.findAll();
        Date now = new Date();

        for (Flight flight : flights) {
            if (flight.getDepartureDate().before(now) && flight.getArrivalDate().after(now)) {
                flight.setStatus(FlightStatus.ONGOING);
            } else if (flight.getArrivalDate().before(now)) {
                flight.setStatus(FlightStatus.COMPLETED);
            }
            flightRepo.save(flight); // Save updated status
        }
    }

    @Override
    public Flight addFlight(Flight flight) {
        if (flightRepo.existsFlightByflightNumber(flight.getFlightNumber())) {
            throw new CustomException("There is already a flight with this flight number");
        }

        // Utilisez le service de géocodage pour obtenir les coordonnées
        double[] departureCoords = geocodingService.getCoordinates(flight.getDeparture());
        double[] destinationCoords = geocodingService.getCoordinates(flight.getDestination());

        // Calculer la date d'arrivée estimée
        Date estimatedArrivalDate = ArrivalDateCalculator.calculateEstimatedArrivalDate(
                departureCoords, destinationCoords, flight.getDepartureDate());
        flight.setArrivalDate(estimatedArrivalDate);
        String  flightDurationMillis = ArrivalDateCalculator.calculateDuration(
                departureCoords, destinationCoords);
          flight.setDuration(flightDurationMillis);


        // Calculer la date d'arrivée de retour
        Date estimatedReturnArrivalDate = ArrivalDateCalculator.calculateEstimatedArrivalDate(
                departureCoords, destinationCoords, flight.getReturnDate());
        flight.setReturnArrivalDate(estimatedReturnArrivalDate);

        //// price

        flight.setPremiumEconomyPrice(flight.getEconomyPrice() * 1.25);
        flight.setBusinessPrice(flight.getEconomyPrice() * 2);
        flight.setFirstClassPrice(flight.getEconomyPrice() * 4);


        return flightRepo.save(flight);
    }

    @Override
    public void deleteFlight(String flightId) {
        if (!flightRepo.existsById(flightId)) {
            throw new CustomException("Flight not found");
        }
        flightRepo.deleteById(flightId);

    }

    @Override
    public Flight updateFlight(Flight flight) {

        flight.setDeparture(flight.getDeparture());
        flight.setDestination(flight.getDestination());
        flight.setDepartureDate(flight.getDepartureDate());
        flight.setArrivalDate(flight.getArrivalDate());

        // Utilisez le service de géocodage pour obtenir les coordonnées
        double[] departureCoords = geocodingService.getCoordinates(flight.getDeparture());
        double[] destinationCoords = geocodingService.getCoordinates(flight.getDestination());

        // Calculer la date d'arrivée estimée
        Date estimatedArrivalDate = ArrivalDateCalculator.calculateEstimatedArrivalDate(
                departureCoords, destinationCoords, flight.getDepartureDate());
        flight.setArrivalDate(estimatedArrivalDate);
        String  flightDurationMillis = ArrivalDateCalculator.calculateDuration(
                departureCoords, destinationCoords);
        flight.setDuration(flightDurationMillis);

        // Calculer la date d'arrivée de retour
        Date estimatedReturnArrivalDate = ArrivalDateCalculator.calculateEstimatedArrivalDate(
                departureCoords, destinationCoords, flight.getReturnDate());
        flight.setReturnArrivalDate(estimatedReturnArrivalDate);

        return flightRepo.save(flight);
    }

    public List<Flight> searchFlightsByClassAndPrice(FlightClass flightClass) {
        switch (flightClass) {
            case ECONOMY:
                return flightRepo.findByFlightClassesContainingOrderByEconomyPriceAsc(flightClass);
            case BUSINESS:
                return flightRepo.findByFlightClassesContainingOrderByBusinessPriceAsc(flightClass);
            case PREMIUM_ECONOMY:
                return flightRepo.findByFlightClassesContainingOrderByPremiumEconomyPriceAsc(flightClass);
            case FIRST:
                return flightRepo.findByFlightClassesContainingOrderByFirstClassPriceAsc(flightClass);
            default:
                throw new IllegalArgumentException("Invalid flight class");
        }
    }

    public double calculateFinalPrice(String idFlight, FlightClass flightClass) {
       Flight flight = flightRepo.findById(idFlight).orElseThrow() ;
        double basePrice;
        logger.info("flight" + flight);
        switch (flightClass) {
            case ECONOMY:
                basePrice = flight.getEconomyPrice();
                break;
            case BUSINESS:
                basePrice = flight.getBusinessPrice();
                break;
            case PREMIUM_ECONOMY:
                basePrice = flight.getPremiumEconomyPrice();
                break;
            case FIRST:
                basePrice = flight.getFirstClassPrice();
                break;
            default:
                throw new IllegalArgumentException("Invalid flight class");
        }
        // Ajouter les frais de bagage si applicable
        double baggageFee = 50.0;
        double finalPrice = basePrice + baggageFee;
        flight.setFinalPrice(finalPrice);
        flightRepo.save(flight);
        return finalPrice;
    }


}
