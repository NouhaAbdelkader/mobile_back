package tn.talan.tripaura_backend.controllers.circuitController;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.talan.tripaura_backend.entities.Circuit.Circuit;
import tn.talan.tripaura_backend.entities.Circuit.Programme;
import tn.talan.tripaura_backend.entities.Flights.Flight;
import tn.talan.tripaura_backend.exceptions.CustomException;
import tn.talan.tripaura_backend.services.CircuitService.CircuitGenerationService;
import tn.talan.tripaura_backend.services.CircuitService.CircuitService;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@Tag(name = "circuit")
@RequestMapping("/circuit")
public class CircuitController {
    private final CircuitService circuitService;

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCircuit(@PathVariable String id) {
        circuitService.deleteCircuit(id);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/programmes/{id}")
    public ResponseEntity<Void> deleteProgramme(@PathVariable String id) {
        circuitService.deleteProgramme(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Circuit> getCircuitById(@PathVariable String id) {
        Circuit circuit = circuitService.getCircuitById(id);
        if (circuit != null) {
            return ResponseEntity.ok(circuit);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/CircuitPersonnalisé/{id}")
    public ResponseEntity<List<Circuit>> getCircuitPById(@PathVariable String id) {
        List<Circuit> circuit = circuitService.getCircuitBuUser(id);
        if (circuit != null) {
            return ResponseEntity.ok(circuit);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<Circuit>> getAllCircuits() {
        List<Circuit> circuits = circuitService.getAllCircuits();
        return ResponseEntity.ok(circuits);
    }


   private final ObjectMapper objectMapper;
    @PostMapping(value = "/with-programmes", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addCircuitWithProgrammes(
            @RequestPart("circuit") String circuitJson,
            @RequestPart(value = "galleryImages", required = false) List<MultipartFile> galleryImages) {
        try {
            Circuit circuit = objectMapper.readValue(circuitJson, Circuit.class);
            Circuit savedCircuit = circuitService.addCircuitWithProgrammes(circuit, galleryImages);
            return new ResponseEntity<>(savedCircuit, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            // Créer un objet d'erreur avec le message d'exception
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // Gérer les autres types d'exceptions
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "An unexpected error occurred: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCircuitWithProgrammes(@PathVariable String id, @RequestBody Circuit circuit) {
        try {
            circuit.setId(id);
            Circuit updatedCircuit = circuitService.updateCircuitWithProgrammes(circuit);
            return new ResponseEntity<>(updatedCircuit, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            // Créer un objet d'erreur avec le message d'exception
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // Gérer les autres types d'exceptions
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "An unexpected error occurred: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/circuit-Programmes/{circuitId}")
    public ResponseEntity<List<Programme>> getProgrammesByCircuit(@PathVariable String circuitId) {
        try {
            List<Programme> programmes = circuitService.findProgByCircuit(circuitId);
            return new ResponseEntity<>(programmes, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getFlightByDepartureAndDestination")
    public ResponseEntity<List<Flight>> getFlightsByDepartureAndDestination(
            @RequestParam String departure,
            @RequestParam String destination) {
        List<Flight> flights = circuitService.getFlightByDepartureAndDestination(departure, destination);
        return ResponseEntity.ok(flights);
    }

    @GetMapping("/filterCircuits")
    public ResponseEntity<List<Circuit>> filterCircuits(
            @RequestParam(required = false) String countryName,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String theme,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Date startDate,
            @RequestParam(required = false) Date endDate) {
        List<Circuit> circuits = circuitService.filterCircuits(countryName, city, theme, type, startDate, endDate);
        return ResponseEntity.ok(circuits);
    }

    private CircuitGenerationService circuitGenerationService;

    @PostMapping("/generate")
    public ResponseEntity<?> generateCustomCircuit(@RequestParam String userId,
                                                   @RequestParam String countryName,
                                                   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
                                                   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
                                                   @RequestParam String countryDepartName) {
        try {
            Circuit circuit = circuitGenerationService.generateCustomCircuit(userId, countryName, startDate, endDate, countryDepartName);
            return new ResponseEntity<>(circuit, HttpStatus.CREATED);
        } catch (CustomException ex) {
            // Créer un objet d'erreur avec le message d'exception personnalisée
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", ex.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException ex) {
            // Gérer les erreurs liées aux arguments non valides
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", ex.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            // Gérer les autres types d'exceptions inattendues
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "An unexpected error occurred: " + ex.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
