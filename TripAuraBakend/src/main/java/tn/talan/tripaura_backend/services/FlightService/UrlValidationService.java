package tn.talan.tripaura_backend.services.FlightService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UrlValidationService {

   private final RestTemplate restTemplate;

    public UrlValidationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean isUrlValid(String url) {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            return false;
        }
    }
}
