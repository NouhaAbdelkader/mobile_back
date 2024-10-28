package tn.talan.tripaura_backend.repositories.Circuit;

import org.springframework.data.mongodb.repository.MongoRepository;
import tn.talan.tripaura_backend.entities.Circuit.Circuit;
import tn.talan.tripaura_backend.entities.Circuit.Country;
import tn.talan.tripaura_backend.entities.UserTripAura;
import tn.talan.tripaura_backend.repositories.UserTripAuraRepo;

import java.util.List;

public interface CircuitRepo extends MongoRepository<Circuit, String> {
    Circuit findCircuitById(String id);
    Circuit findCircuitByProgrammes_Id(String programmeId );
    List<Circuit> findCircuitByOrderByDateDeCreationDesc();
    List<Circuit> findCircuitByCountry(Country country);
    List<Circuit> findCircuitByUserPersonnalizedOrderByDateDeCreationDesc(UserTripAura user);

}
