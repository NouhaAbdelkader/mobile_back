package tn.talan.tripaura_backend.services.ReservationService;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.talan.tripaura_backend.entities.Circuit.Circuit;
import tn.talan.tripaura_backend.entities.Circuit.Programme;
import tn.talan.tripaura_backend.entities.Flights.FlightClass;
import tn.talan.tripaura_backend.entities.Reseravation.Reservation;
import tn.talan.tripaura_backend.entities.UserTripAura;
import tn.talan.tripaura_backend.entities.activities.ActivityTripAura;
import tn.talan.tripaura_backend.repositories.Circuit.CircuitRepo;
import tn.talan.tripaura_backend.repositories.ReservationRepo.ReservationRepo;
import tn.talan.tripaura_backend.repositories.UserTripAuraRepo;
import tn.talan.tripaura_backend.services.FlightService.FlightService;

import java.util.List;

@Service
@AllArgsConstructor
public class ReservationImpl {
    private ReservationRepo reservationRepository;
    private CircuitRepo circuitRepo;
    private UserTripAuraRepo   userRepo;
    private FlightService flightService;
    // Méthode pour calculer le prix total
    public Reservation calculerPrixTotal(Reservation reservation) {
        float prixTotal = 0.0f;
        float jours = 0.0f;
        double flightPrice = 0 ;

        // Récupérer le circuit complet à partir de l'ID
        Circuit circuit = circuitRepo.findCircuitById(reservation.getCircuit().getId());

        // flight price

        flightPrice = flightService.calculateFinalPrice(circuit.getVol().getId() , FlightClass.ECONOMY);
        prixTotal += (float) flightPrice;

        // Ajouter le prix des hébergements dans le circuit
        List<Programme> programmes = circuit.getProgrammes();
        if (programmes != null) {
            for (Programme programme : programmes) {
                long nbJours = (programme.getEndDate().getTime() - programme.getStartDate().getTime()) / (1000 * 60 * 60 * 24);
                 System.out.println("nbJours"+nbJours);
                if (programme.getHebergement() != null) {

                    prixTotal += programme.getHebergement().getPrix() * nbJours;
                    System.out.println("prix hebergement"+prixTotal);
                }

                // Ajouter le prix des activités dans chaque programme
                List<ActivityTripAura> activities = programme.getActivities();
                if (activities != null) {
                    for (ActivityTripAura activity : activities) {
                        prixTotal += activity.getPrice();
                    }
                }

                if (programme.getVol() != null) {

                        prixTotal += (float) flightService.calculateFinalPrice(programme.getVol().getId(), FlightClass.ECONOMY);
                    }
            }
        }

        // Ajuster en fonction du nombre de personnes
        prixTotal *= reservation.getNbPersonne();
        reservation.setPrix(prixTotal);
        reservation.setDateDebut(circuit.getDateDepart());
        reservation.setDatefin(circuit.getDateArrive());

        return reservation;
    }

    public List<Reservation> getReservationByUser(String id) {
        UserTripAura u = userRepo.findUserTripAuraById(id);
        return reservationRepository.findReservationByUser(u);
    }
    






}
