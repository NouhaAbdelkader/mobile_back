package tn.talan.tripaura_backend.services.CircuitService;

import tn.talan.tripaura_backend.entities.Circuit.Destination;

import java.util.List;

public interface DestinationService {
    public Destination addDest(Destination dest);
    public void deleteDest(String destId);
    public List<Destination> getAllDest();

}
