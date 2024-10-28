package tn.talan.tripaura_backend.services.FlightService.GeocodingService;

import java.util.List;

public class OpenCageResponse {
    private List<OpenCageResult> results;

    public List<OpenCageResult> getResults() {
        return results;
    }

    public void setResults(List<OpenCageResult> results) {
        this.results = results;
    }
}
