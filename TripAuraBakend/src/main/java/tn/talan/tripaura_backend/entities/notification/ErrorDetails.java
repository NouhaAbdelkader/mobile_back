package tn.talan.tripaura_backend.entities.notification;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor

public class ErrorDetails implements Serializable {

    @Indexed
    LocalDate timestamp;

    @Indexed
    String message;

    @Indexed
    String details;

}
