package tn.talan.tripaura_backend.controllers.accommodationController;


import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.io.IOException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.talan.tripaura_backend.entities.accommodation.Accommodation;

import com.fasterxml.jackson.databind.ObjectMapper;
import tn.talan.tripaura_backend.entities.accommodation.AccommodationType;
import tn.talan.tripaura_backend.repositories.accommodationRepo.AccommodationRepo;
import tn.talan.tripaura_backend.services.accomodationService.AccomodationService;

import java.util.List;

@RestController
@AllArgsConstructor
@Tag(name = "accommodations")
@RequestMapping("/accommodations")
public class AccomodationController {

    private final AccomodationService accomodationService;
   private final ObjectMapper objectMapper;
 private AccommodationRepo accommodationRepo ;
    private static final Logger logger = LoggerFactory.getLogger(AccomodationService.class);


    @PostMapping(value = "/accommodations/create",  consumes = "multipart/form-data")
    public ResponseEntity<?> createAccommodation(
            @RequestPart("accommodation") String accommodationJson,
            @RequestParam("image") MultipartFile image,
            @RequestParam("fileType") String fileType) {
        // Validate the accommodation JSON to ensure it's provided
        if (accommodationJson == null || accommodationJson.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Accommodation data must be provided.");
        }

        try {
            Accommodation accommodation = objectMapper.readValue(accommodationJson, Accommodation.class);
            accommodationRepo.save(accommodation);
            logger.info("Creating accommodation: {}", accommodation.getId());
            ResponseEntity<?> response = accomodationService.createAccomodation(accommodation, image, fileType);
            if (response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError()) {
                return response;
            }
            return new ResponseEntity<>(response.getBody(), HttpStatus.CREATED);
        } catch (IOException | JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error parsing accommodation JSON: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAccommodation(@PathVariable String id) {
        try {
            accomodationService.deleteAccomodation(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Accommodation not found with id: " + id);
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Accommodation>> getAllAccommodations() {
        List<Accommodation> accommodations = accomodationService.getAllAccommodation();
        if (accommodations.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(accommodations, HttpStatus.OK);


    }
    @GetMapping("/media/{mediaId}")
    public ResponseEntity<byte[]> getMedia(@PathVariable String mediaId) {
        try {
            byte[] mediaBytes = accomodationService.getMediaAsBytes(mediaId);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG); // Or whatever the actual type is
            return new ResponseEntity<>(mediaBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            logger.error("Error retrieving media with ID: {}", mediaId, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/createAccomm")
    public ResponseEntity<?> createAccommodation(@RequestBody Accommodation accommodation) {
        ResponseEntity<?> response = accomodationService.addAccomodation(accommodation);
        if (response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError()) {
            // If there's an error (either client or server), return the response as is
            return response;
        }
        // If the operation was successful, return the created accommodation with status 201 (CREATED)
        return new ResponseEntity<>(response.getBody(), HttpStatus.CREATED);
    }

    @GetMapping("/accommodations/{id}")
    public ResponseEntity<Accommodation> getAccommodationById(@PathVariable String id) {
        Accommodation accommodation = accomodationService.getAccommodationById(id);
        if (accommodation != null) {
            return new ResponseEntity<>(accommodation, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping("/accommodations")
    public ResponseEntity<?> updateAccommodation(@RequestBody Accommodation accommodation) {
        return accomodationService.updateAccommodation(accommodation);
    }

    @GetMapping("/accommodations/type/{type}")
    public List<Accommodation> getAllAccommodationsByType(@PathVariable AccommodationType type) {
        return accomodationService.getAllAccommodationByType(type);
    }
    @GetMapping("/accommodations/city/{type}")
    public List<Accommodation> getAllAccommodationsByCity(@PathVariable String type) {
        return accomodationService.getAllAccommodationByCity(type);
    }

    @GetMapping("/accommodations/full")
    public List<Accommodation> getAllAccommodationsFull() {
        return accomodationService.getAllAccommodationFull();
    }
    @GetMapping("/accommodations/notFull")
    public List<Accommodation> getAllAccommodationsNotfull() {
        return accomodationService.getAllAccommodationNotFull();
    }
    @GetMapping("/accommodations/{type}/{city}")
    public List<Accommodation> getAllAccommodationsNotfull(@PathVariable AccommodationType type,@PathVariable String city) {
        return accomodationService.getAllAccommodationByTypeAndCity(type, city);
    }

    @GetMapping("/accommodations/Rate/{rate}")
    public List<Accommodation> getAllAccommodationsByCity(@PathVariable int rate) {
        return accomodationService.findAccommodationByRateNumber(rate);
    }
    @GetMapping("/filterAccommodations")
    public List<Accommodation> filterAccommodations(
            @RequestParam(required = false) String countryName,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) Integer rateNumber,
            @RequestParam(required = false) Boolean full,
            @RequestParam(required = false) AccommodationType accomType) {
        return accomodationService.filterAccommodations(countryName,city, rateNumber, full, accomType);
    }



    }
