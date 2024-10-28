package tn.talan.tripaura_backend.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

//second commit
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Test")
public class Test {
    @Id
    private String id;
    @Indexed
    private String name;

}