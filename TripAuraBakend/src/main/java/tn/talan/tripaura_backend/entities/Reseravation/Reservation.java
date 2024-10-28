package tn.talan.tripaura_backend.entities.Reseravation;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import tn.talan.tripaura_backend.entities.Circuit.Circuit;
import tn.talan.tripaura_backend.entities.UserTripAura;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor

@Document(collection = "Reservation")
public class Reservation {
    @Id
    private String id;

    @Indexed
    private int nbPersonne;
    @Indexed
    private float prix;

    @DBRef
    @Indexed
    private UserTripAura user ;
    @DBRef
    @Indexed
    private Circuit circuit ;
    @Indexed
    private Date dateDebut;
    @Indexed
    private Date datefin;


}
