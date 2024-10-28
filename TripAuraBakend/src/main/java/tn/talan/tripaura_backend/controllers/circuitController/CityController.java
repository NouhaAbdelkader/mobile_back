package tn.talan.tripaura_backend.controllers.circuitController;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.talan.tripaura_backend.entities.Circuit.City;
import tn.talan.tripaura_backend.services.CircuitService.CityService;

import java.util.List;

@RestController
@AllArgsConstructor
@Tag(name = "City")
@RequestMapping("/City")
public class CityController {
    private final CityService cityService;


    @PostMapping("/{countryId}")
    public ResponseEntity<City> addCity(@RequestBody City city, @RequestParam String countryId) {
        try {
            City createdCity = cityService.addCity(city, countryId);
            return new ResponseEntity<>(createdCity, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/by-country/{countryId}")
    public ResponseEntity<List<City>> getCityByCountry(@PathVariable String countryId) {
        try {
            List<City> cities = cityService.getCityByCountry(countryId);
            return new ResponseEntity<>(cities, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
