package tn.talan.tripaura_backend.controllers.FlightController;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.talan.tripaura_backend.entities.Flights.Airline;
import tn.talan.tripaura_backend.exceptions.CustomException;
import tn.talan.tripaura_backend.services.FlightService.AirlineService;

import java.io.IOException;
import java.util.List;

@RestController
@Validated
@AllArgsConstructor
@CrossOrigin
@Tag(name = "Airline Management")
@RequestMapping("/airlines")
public class AirlineController {

    private final AirlineService airlineService;

    @Operation(description = "Add a new airline")
    @PostMapping("/add")
    public ResponseEntity<Airline> addAirline(@RequestPart("airline") Airline airline,
                                              @RequestPart("logo") MultipartFile logoFile) {
        Airline createdAirline = airlineService.addAirline(airline, logoFile);
        return new ResponseEntity<>(createdAirline, HttpStatus.CREATED);
    }
    @GetMapping("/logo/{id}")
    public ResponseEntity<byte[]> getAirlineLogo(@PathVariable String id) {
        try {
            byte[] image = airlineService.getLogoAsBytes(id);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG); // ou autre type d'image, selon le format stocké
            return new ResponseEntity<>(image, headers, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<String> handleCustomException(CustomException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @Operation(description = "Get all airlines")
    @GetMapping("/all")
    public ResponseEntity<List<Airline>> getAllAirlines() {
        List<Airline> airlines = airlineService.getAirlines();
        return new ResponseEntity<>(airlines, HttpStatus.OK);
    }

    @Operation(description = "Search for airlines")
    @GetMapping("/search")
    public ResponseEntity<List<Airline>> searchAirline(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String code,
            @RequestParam (required = false)  String country)
            {
        System.out.println("Received Params - name: " + name + ", code: " + code + ", country: " + country);

        List<Airline> airlines = airlineService.searchAirlines(name, code, country);

        return ResponseEntity.ok(airlines);
    }

    @Operation(description = "Update an airline")
    @PutMapping("/update")
    public ResponseEntity<Airline> updateAirline(
            @RequestPart("airline") String airlineJson,
            @RequestPart(value = "logo", required = false) MultipartFile logoFile) {

        // Convertir la chaîne JSON en objet Airline
        Airline airline;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            airline = objectMapper.readValue(airlineJson, Airline.class);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }

        // Mise à jour de la compagnie aérienne, en tenant compte du logo s'il est fourni
        Airline updatedAirline = airlineService.updateAirline(airline, logoFile);
        return ResponseEntity.ok(updatedAirline);
    }


    @Operation(description = "Delete an airline")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteAirline(@PathVariable String id) {
        airlineService.deleteAirline(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content for successful deletion
    }
}
