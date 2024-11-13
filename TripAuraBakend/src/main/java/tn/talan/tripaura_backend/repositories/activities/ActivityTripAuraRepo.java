package tn.talan.tripaura_backend.repositories.activities;

import org.springframework.data.mongodb.repository.MongoRepository;
import tn.talan.tripaura_backend.entities.activities.ActivityTripAura;
import tn.talan.tripaura_backend.entities.activities.TypeActivity;

import java.util.List;

public interface ActivityTripAuraRepo extends MongoRepository<ActivityTripAura,String> {


    public Boolean existsActivityTripAurasByName(String name);
    List<ActivityTripAura> findByNameIn(List<String> noms);
    ActivityTripAura findActivityTripAuraByName(String name);

    List<ActivityTripAura> findByTypeActivity(TypeActivity typeActivity);


}
