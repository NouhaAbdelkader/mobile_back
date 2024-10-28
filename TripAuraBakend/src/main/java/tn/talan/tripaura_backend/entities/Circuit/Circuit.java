package tn.talan.tripaura_backend.entities.Circuit;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.persistence.Id;
import tn.talan.tripaura_backend.entities.Flights.Flight;
import tn.talan.tripaura_backend.entities.UserTripAura;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor

@Document(collection = "Circuit")

public class Circuit {
    @Id
    private String id;

@Indexed
    private String titre;
    @Indexed

 private Date dateDeCreation;

    @Indexed
    private Theme themee;

    @Indexed
    private TypeCircuit type;
    @DBRef
    @Indexed
    private Flight vol;

    private String volNumber ;
    @Indexed
    private boolean personalized= false;
    @DBRef
    @Indexed
    private UserTripAura userPersonnalized = null ;
    @Indexed
    private Date dateDepart;
    @Indexed
    private Date dateArrive;

    @Indexed
    @DBRef
    private Country country;

    private String countryName ;




    @Indexed
    private List<String> gallerie  ;
    @Indexed
    @DBRef
    private Country countryDeparture ;

@Indexed
    @DBRef
    private List<Programme> programmes;
}