package tn.talan.tripaura_backend.controllers.payment;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import tn.talan.tripaura_backend.entities.Reseravation.Reservation;
@Getter
@Setter
@NoArgsConstructor
@Document(collection = "confirgReservationRequest")

public class ConfirmReservationRequest {
    @Id
    private String id;
    private String sessionId;
    private Reservation reservationDetails;
}
