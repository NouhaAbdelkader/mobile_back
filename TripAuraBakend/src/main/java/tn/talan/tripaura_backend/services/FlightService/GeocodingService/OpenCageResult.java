package tn.talan.tripaura_backend.services.FlightService.GeocodingService;

class OpenCageResult {
    private OpenCageGeometry geometry;

    public OpenCageGeometry getGeometry() {
        return geometry;
    }

    public void setGeometry(OpenCageGeometry geometry) {
        this.geometry = geometry;
    }
}