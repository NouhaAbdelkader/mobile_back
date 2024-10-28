package tn.talan.tripaura_backend.entities.activities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Document(collection = "ActivityTripAura")
@FieldDefaults(level = AccessLevel.PRIVATE)

public class ActivityTripAura implements Serializable {

    @Id
    String id;

    @Transient
    @NonNull
    @Indexed
    String name;

    @Transient
    @NonNull
    @Indexed
    Date dateOfBegining;

    @Transient
    @NonNull
    @Indexed
    Date dateOfEnding;

    @Enumerated(EnumType.STRING)
    @NonNull
    @Indexed
    @Transient
    TypeActivity typeActivity;

    @Transient
    @Indexed
    @NonNull
    int price;
    @Indexed
    String image ;


}
