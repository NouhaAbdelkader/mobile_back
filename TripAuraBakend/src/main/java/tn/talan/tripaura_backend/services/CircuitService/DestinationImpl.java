package tn.talan.tripaura_backend.services.CircuitService;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.talan.tripaura_backend.entities.Circuit.Destination;
import tn.talan.tripaura_backend.repositories.Circuit.DestinationRepo;

import java.util.List;
@Service
@AllArgsConstructor
public class DestinationImpl implements  DestinationService{
    public DestinationRepo destinationRepo ;
    @Override
    public Destination addDest(Destination dest) {
        return destinationRepo.save(dest);
    }

    @Override
    public void deleteDest(String destId) {
        Destination dest= destinationRepo.findDestinationById(destId);
         destinationRepo.delete(dest);
    }

    @Override
    public List<Destination> getAllDest() {
        return destinationRepo.findAll();
    }
}
