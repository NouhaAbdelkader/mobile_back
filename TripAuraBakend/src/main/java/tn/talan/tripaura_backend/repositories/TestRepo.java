package tn.talan.tripaura_backend.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import tn.talan.tripaura_backend.entities.Test;

public interface TestRepo extends MongoRepository<Test,String> {


}
