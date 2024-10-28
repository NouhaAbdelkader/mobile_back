package tn.talan.tripaura_backend.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.talan.tripaura_backend.entities.Test;
import tn.talan.tripaura_backend.repositories.TestRepo;

import java.util.List;
//first commit
@Service
@AllArgsConstructor
public class ServicesTest {
    TestRepo testRepo ;
    public List<Test> getAllModule() {

        return testRepo.findAll();

    }
}
