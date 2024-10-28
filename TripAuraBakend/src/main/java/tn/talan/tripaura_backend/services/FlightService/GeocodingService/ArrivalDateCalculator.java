package tn.talan.tripaura_backend.services.FlightService.GeocodingService;

import java.util.Date;

public class  ArrivalDateCalculator {
    private static final double AVERAGE_FLIGHT_SPEED_KMH = 800; // Vitesse moyenne de vol

    public static Date calculateEstimatedArrivalDate(double[] departureCoords, double[] destinationCoords, Date departureDate) {
        double distance = DistanceCalculator.calculateDistance(departureCoords, destinationCoords);
        double flightDurationHours = distance / AVERAGE_FLIGHT_SPEED_KMH;

        long flightDurationMillis = (long) (flightDurationHours * 3600 * 1000);

        return new Date(departureDate.getTime() + flightDurationMillis);
    }

    public static String calculateDuration(double[] departureCoords, double[] destinationCoords) {
        double distance = DistanceCalculator.calculateDistance(departureCoords, destinationCoords);
        double flightDurationHours = distance / AVERAGE_FLIGHT_SPEED_KMH;
        long flightDurationMillis = (long) (flightDurationHours * 3600 * 1000);

        long hours = flightDurationMillis / (3600 * 1000);
        long minutes = (flightDurationMillis % (3600 * 1000)) / (60 * 1000);

        return String.format("%02dh %02dm", hours, minutes);
    }

}