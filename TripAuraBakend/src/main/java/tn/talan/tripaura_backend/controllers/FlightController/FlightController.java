package tn.talan.tripaura_backend.controllers.FlightController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tn.talan.tripaura_backend.entities.Flights.Flight;
import tn.talan.tripaura_backend.entities.Flights.FlightClass;
import tn.talan.tripaura_backend.entities.Flights.FlightType;
import tn.talan.tripaura_backend.exceptions.CustomException;
import tn.talan.tripaura_backend.services.FlightService.FlightService;

import java.util.Date;
import java.util.List;

@RestController
@Validated
@AllArgsConstructor
@CrossOrigin
@Tag(name = "Flight")
@RequestMapping("/flights")
public class FlightController {
    private final FlightService flightService;

    @Operation(description = "Add a new flight")
    @PostMapping("/add")
    public ResponseEntity<Flight> addFlight(@Valid @RequestBody Flight flight) {
        Flight addedFlight = flightService.addFlight(flight);
        return new ResponseEntity<>(addedFlight, HttpStatus.CREATED);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<String> handleCustomException(CustomException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
    @Operation(description = "Search for flights")
    @GetMapping("/search")
    public ResponseEntity<List<Flight>> searchFlights(
            @RequestParam(required = false) String departure,
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date departureDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date returnDate,
            @RequestParam (required = false)FlightClass flightClass,
            @RequestParam (required = false)FlightType flightType) {
        System.out.println("Received Params - Departure: " + departure + ", Destination: " + destination + ", Departure Date: " + departureDate + ", Return Date: " + returnDate + ", Flight Class: " + flightClass + ", Flight Type: " + flightType);

        List<Flight> flights = flightService.searchFlights(departure, destination, departureDate, returnDate, flightClass, flightType);

        return ResponseEntity.ok(flights);
    }
    @Operation(description = "Manually update flight statuses")
    @PostMapping("/updateStatuses")
    public ResponseEntity<String> updateStatuses() {
        flightService.updateFlightStatus();
        return new ResponseEntity<>("Flight statuses updated successfully", HttpStatus.OK);

    }
    @Operation(description = "Get all flights")
    @GetMapping("/all")
    public ResponseEntity<List<Flight>> getAllFlights() {
        List<Flight> flights = flightService.getFlights();
        return new ResponseEntity<>(flights, HttpStatus.OK);
    }


    @Operation(description = "Update a flight")
    @PutMapping("/update")
    public ResponseEntity<Flight> updateFlight(@RequestBody Flight flight) {
        Flight updatedFlight = flightService.updateFlight(flight);
        return ResponseEntity.ok(updatedFlight);
    }

    @Operation(description = "Delete a flight")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteFlight(@PathVariable String id) {
        flightService.deleteFlight(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content for successful deletion
    }

    @GetMapping("/search-flights")
    public List<Flight> searchFlights(@RequestParam FlightClass flightClass) {
        return flightService.searchFlightsByClassAndPrice(flightClass);
    }
    @PostMapping("/calculate-price")
    public ResponseEntity<Double> calculateFinalPrice(
            @RequestParam String idFlight,
            @RequestParam FlightClass flightClass) {

        double updatedFlight = flightService.calculateFinalPrice(idFlight, flightClass);
        return ResponseEntity.ok(updatedFlight);
    }
    }
