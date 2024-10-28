package tn.talan.tripaura_backend.entities.Flights;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "flights")
public class Flight implements Serializable {
    @Id
    private String id;

    @Indexed
    @NotEmpty(message = "Flight number cannot be empty")
    private String flightNumber;

    @Indexed
    private String departure;

    @Indexed
    private String destination;

    @Indexed
    @FutureOrPresent(message = "Departure date cannot be in the past")
    private Date departureDate;

    @Indexed
    private Date ArrivalDate;

    @Indexed
    private String duration ;
    @Indexed
     @FutureOrPresent(message = "Return date cannot be in the past")
    private Date returnDate;

    @Indexed
    private Date returnArrivalDate;

    @Indexed
    private Set<FlightClass> flightClasses = new HashSet<>();
    {
        Collections.addAll(flightClasses, FlightClass.ECONOMY,FlightClass.BUSINESS , FlightClass.PREMIUM_ECONOMY , FlightClass.FIRST);
    }
    @Indexed
    private Set<FlightType> flightTypes = new HashSet<>();

    {
        Collections.addAll(flightTypes, FlightType.MULTI_CITY, FlightType.ONE_WAY ,FlightType.ROUND_TRIP);
    }
    @Indexed
    private Set<BaggageOption> baggageOptions = new HashSet<>();

    {
        Collections.addAll(baggageOptions, BaggageOption.WITHOUT_BAGGAGE, BaggageOption.WITH_BAGGAGE);
    }
    @DBRef
    @NotNull(message = "Airline cannot be null")
    private Airline airline;

    @Indexed
    public FlightStatus status = FlightStatus.SCHEDULED; // Default status


    // Add fields for prices based on class
    @Indexed
    private double economyPrice;
    @Indexed
    private double businessPrice;
    @Indexed
    private double premiumEconomyPrice;
    @Indexed
    private double firstClassPrice;

    @Indexed
    private double finalPrice;

}
