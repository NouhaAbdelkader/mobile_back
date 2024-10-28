package tn.talan.tripaura_backend.services.FlightService.GeocodingService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GeocodingService {

    @Value("${opencage.api.key}")
    private String apiKey;

    private static final String API_URL = "https://api.opencagedata.com/geocode/v1/json?q=%s&key=%s";

    public double[] getCoordinates(String cityName) {
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format(API_URL, cityName, apiKey);

        OpenCageResponse response = restTemplate.getForObject(url, OpenCageResponse.class);
        if (response != null && response.getResults() != null && !response.getResults().isEmpty()) {
            OpenCageResult result = response.getResults().get(0);
            double lat = result.getGeometry().getLat();
            double lng = result.getGeometry().getLng();
            return new double[]{lat, lng};
        }
        throw new IllegalArgumentException("City not found");
    }
}