package tn.talan.tripaura_backend.entities.Circuit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.persistence.Id;
@Getter
@Setter
@NoArgsConstructor

@Document(collection = "City")
@JsonIgnoreProperties({ "country"})
public class City {
    @Id
    private String id;
@Indexed
    private String nom;
    @Indexed
private  double lat ;
    @Indexed
    private  double lng ;

@DBRef
    private Country country;
}
