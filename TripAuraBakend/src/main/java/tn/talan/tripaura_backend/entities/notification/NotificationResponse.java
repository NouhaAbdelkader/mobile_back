package tn.talan.tripaura_backend.entities.notification;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "")
public class NotificationResponse {

    private int status;
    private String message;

    public NotificationResponse(int Status, String message ) {
        this.status = status;
        this.message = message;
    }
}
