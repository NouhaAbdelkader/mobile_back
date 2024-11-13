package tn.talan.tripaura_backend.entities.Circuit;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.persistence.Id;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor

@Document(collection = "Country")
public class Country {
@Id()
    private String id;
    @Indexed

    private String nom;
    @Indexed
    @DBRef
    private List<City> cities;

    // Getters and Setters
}

