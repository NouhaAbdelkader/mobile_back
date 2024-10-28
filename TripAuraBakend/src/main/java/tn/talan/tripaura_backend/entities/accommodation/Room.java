package tn.talan.tripaura_backend.entities.accommodation;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "Room")
public class Room implements Serializable {
    @Id
    private String id;
    @Indexed
    @NotEmpty(message = "Room number cannot be empty")
    private int roomNumber ;
    @Indexed
    private boolean reserved ;
    @Indexed
    private RoomType roomType;
    @Indexed
    private  ViewType viewType;
    @Indexed
    private boolean cribAvailable= false;
    @Indexed
    private Date date ;
    @Indexed
    @DBRef
    private Accommodation accommodation;
}
