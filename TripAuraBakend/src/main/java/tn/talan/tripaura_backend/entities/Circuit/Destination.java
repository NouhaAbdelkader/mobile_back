package tn.talan.tripaura_backend.entities.Circuit;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "dest")
public class Destination {
    @Id
    private String id;
    @Indexed
    private String nom;
    @Indexed
    private String description;
    // Getters, setters, and other attributes
}