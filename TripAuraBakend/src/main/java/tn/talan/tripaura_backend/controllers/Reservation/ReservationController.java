package tn.talan.tripaura_backend.controllers.Reservation;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.talan.tripaura_backend.entities.Reseravation.Reservation;
import tn.talan.tripaura_backend.entities.UserTripAura;
import tn.talan.tripaura_backend.repositories.ReservationRepo.ReservationRepo;
import tn.talan.tripaura_backend.services.ReservationService.ReservationImpl;

import java.util.List;

@RestController
@AllArgsConstructor
@Tag(name = "Reservation")
@RequestMapping("/Reservation")
public class ReservationController {
    private ReservationRepo reservationRepository;
    private ReservationImpl reservationService;

    @PostMapping("/calculPrix")
    public ResponseEntity<Reservation> calculPrix(@RequestBody Reservation reservation) {
        Reservation prixTotal = reservationService.calculerPrixTotal(reservation);


        return new ResponseEntity<>(prixTotal, HttpStatus.CREATED);
    }
    @PostMapping("/cerateReservation")
    public ResponseEntity<Reservation> createRservation(@RequestBody Reservation reservation) {
        Reservation prixTotal = reservationService.calculerPrixTotal(reservation);
        reservation.setPrix(prixTotal.getPrix());

        Reservation savedReservation = reservationRepository.save(reservation);

        return new ResponseEntity<>(savedReservation, HttpStatus.CREATED);
    }
    @GetMapping("/getReservationsByUser/{userId}")
    public ResponseEntity<List<Reservation>> getReservationsByUserId(@PathVariable String userId) {
        List<Reservation> reservations = reservationService.getReservationByUser(userId);
        return ResponseEntity.ok(reservations);
    }
}
