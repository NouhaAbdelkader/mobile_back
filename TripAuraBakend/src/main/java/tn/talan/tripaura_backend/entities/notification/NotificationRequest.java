package tn.talan.tripaura_backend.entities.notification;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
@Document(collection = "Notification")
public class NotificationRequest implements Serializable {

    private String title;
    private String body;
    private String topic;
    private String token ;
}
