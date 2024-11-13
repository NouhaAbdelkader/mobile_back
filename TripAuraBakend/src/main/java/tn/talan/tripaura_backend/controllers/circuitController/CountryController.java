package tn.talan.tripaura_backend.controllers.circuitController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.talan.tripaura_backend.entities.Circuit.Country;
import tn.talan.tripaura_backend.services.CircuitService.CountryService;

import java.util.List;

@RestController
@AllArgsConstructor
@Tag(name = "Country")
@RequestMapping("/Country")
public class CountryController {
    private CountryService countryService;

    @PostMapping("/add")
    public ResponseEntity<Country> addCountry(@RequestBody Country country) {
        try {
            Country createdCountry = countryService.addCountry(country);
            return new ResponseEntity<>(createdCountry, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping
    public ResponseEntity<List<Country>> getAllCountries() {
        try {
            List<Country> countries = countryService.getAllCountry();
            return new ResponseEntity<>(countries, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(description = "delete country")
    @DeleteMapping("/delete/{id}")
    public void deleteCountry(@PathVariable String id) {
        countryService.deleteCountry(id);
    }

}
