package tn.talan.tripaura_backend.entities.Flights;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "airlines")
public class Airline implements Serializable {
    @Id
    private String id;

    @Indexed
    @NotEmpty(message = "name cannot be empty")
    private String name;

    @Indexed
    @NotEmpty(message = "code cannot be empty")
    private String code;

    @NotEmpty(message = "country cannot be empty")
    @Indexed
    private String country;

    @NotEmpty(message = "logo cannot be empty")
    @Indexed
    private String  logoUrl; // URL of the airline logo image

    @DBRef
    private List<Flight> flights; // Updated to a list of flights
}