package tn.talan.tripaura_backend.repositories.ReservationRepo;

import org.springframework.data.mongodb.repository.MongoRepository;
import tn.talan.tripaura_backend.entities.Reseravation.Reservation;
import tn.talan.tripaura_backend.entities.UserTripAura;

import java.util.List;

public interface ReservationRepo extends MongoRepository<Reservation, String> {
    Reservation findReservationById(String id);
    List<Reservation> findReservationByUser(UserTripAura user);
}
