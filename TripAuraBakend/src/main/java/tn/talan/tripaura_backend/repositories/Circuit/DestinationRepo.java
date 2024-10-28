package tn.talan.tripaura_backend.repositories.Circuit;

import org.springframework.data.mongodb.repository.MongoRepository;
import tn.talan.tripaura_backend.entities.Circuit.Destination;
import java.util.List;

public interface DestinationRepo extends MongoRepository<Destination, String> {
    Destination findDestinationById(String id);
    List<Destination> findByNomIn(List<String> noms);
}
