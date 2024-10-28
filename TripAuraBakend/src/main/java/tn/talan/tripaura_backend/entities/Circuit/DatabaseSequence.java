package tn.talan.tripaura_backend.entities.Circuit;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;


@Getter
@Setter
@NoArgsConstructor
@Document(collection = "database_sequences")
public class DatabaseSequence  {
    @Id
    private String id;
    private long seq;

    // Getters and setters
}

