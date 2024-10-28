package tn.talan.tripaura_backend.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import tn.talan.tripaura_backend.entities.PasswordResetToken;

public interface PasswordTokenRepository extends MongoRepository<PasswordResetToken, String> {


}
