package tn.talan.tripaura_backend.entities.Circuit;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import tn.talan.tripaura_backend.entities.Flights.Flight;
import tn.talan.tripaura_backend.entities.accommodation.Accommodation;
import tn.talan.tripaura_backend.entities.activities.ActivityTripAura;

import java.util.Date;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor

@Document(collection = "Programme")
@JsonIgnoreProperties({"circuit"})


public class Programme {
    @Id
    private String id= "0";
    public static final String SEQUENCE_NAME = "programmes_sequence";
    @Indexed
    private String titre;
    @Indexed
    private String description;


    @Indexed
    @DBRef
    private City city;
    @Indexed
    private String cityName ;

    @Indexed
    @DBRef
    private Accommodation hebergement = null;

    private  String accommodationName;


    @Indexed
   @DBRef
    private List<ActivityTripAura> activities = null;
    @Indexed
    private  List<String> ActivityName;
    @Indexed
    @DBRef
    private Flight vol = null;
    private  String flightNumber=null;
    @Indexed
    private Date startDate;
    @Indexed
    private Date endDate;
    @DBRef
    private Circuit circuit ;
}
