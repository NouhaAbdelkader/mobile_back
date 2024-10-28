package tn.talan.tripaura_backend.helpers;

import org.springframework.stereotype.Component;
import tn.talan.tripaura_backend.entities.Flights.FlightClass;
import tn.talan.tripaura_backend.entities.Flights.FlightType;
import org.springframework.core.convert.converter.Converter;

@Component
public class Conversions {

    @Component
    public static class StringToFlightClassConverter implements Converter<String, FlightClass> {
        @Override
        public FlightClass convert(String source) {
            try {
                return FlightClass.valueOf(source.toUpperCase());
            } catch (IllegalArgumentException e) {
                return null; // or handle the exception as needed
            }
        }
    }

    @Component
    public static class StringToFlightTypeConverter implements Converter<String, FlightType> {
        @Override
        public FlightType convert(String source) {
            try {
                return FlightType.valueOf(source.toUpperCase());
            } catch (IllegalArgumentException e) {
                return null; // or handle the exception as needed
            }
        }
    }
}
