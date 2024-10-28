package tn.talan.tripaura_backend.controllers.circuitController;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.talan.tripaura_backend.entities.Circuit.Destination;
import tn.talan.tripaura_backend.services.CircuitService.DestinationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Tag(name = "Destination")
@RequestMapping("/Destination")
public class DestinationController {
    private final DestinationService destinationService;

    @PostMapping
    public ResponseEntity<Destination> addDestination(@RequestBody Destination destination) {
        Destination createdDestination = destinationService.addDest(destination);
        return ResponseEntity.ok(createdDestination);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDestination(@PathVariable String id) {
        destinationService.deleteDest(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Destination>> getAllDestinations() {
        List<Destination> destinations = destinationService.getAllDest();
        return ResponseEntity.ok(destinations);
    }
}
