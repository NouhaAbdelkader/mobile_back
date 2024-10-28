package tn.talan.tripaura_backend.repositories.Circuit;

import org.springframework.data.mongodb.repository.MongoRepository;
import tn.talan.tripaura_backend.entities.Circuit.DatabaseSequence;

public interface DatabaseSequenceRepository extends MongoRepository<DatabaseSequence, String> {
}