package tn.talan.tripaura_backend.services.activities;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.talan.tripaura_backend.entities.Flights.Flight;
import tn.talan.tripaura_backend.entities.Flights.FlightClass;
import tn.talan.tripaura_backend.entities.activities.ActivityTripAura;
import tn.talan.tripaura_backend.entities.activities.TypeActivity;
import tn.talan.tripaura_backend.exceptions.CustomException;
import tn.talan.tripaura_backend.repositories.activities.ActivityTripAuraRepo;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ActivityTripAuraImpl implements ActivityTripAuraService {

    private final ActivityTripAuraRepo activityTripAuraRepo;


    @Override
    public List<ActivityTripAura> getAllActivitites() {
        return activityTripAuraRepo.findAll();
    }

    @Override
    public Optional<ActivityTripAura> getActivityById(String id) {
        return activityTripAuraRepo.findById(id);
    }

    @Override
    public ActivityTripAura addActivity(ActivityTripAura activityTripAura) {
        if (activityTripAuraRepo.existsActivityTripAurasByName(activityTripAura.getName())) {
            throw new CustomException("there is already an activity trip Aura with that name");
        } else {
            return activityTripAuraRepo.save(activityTripAura);
        }
    }

    @Override
    public ActivityTripAura updateActivity(ActivityTripAura activityTripAura, String id) {

        ActivityTripAura activityTripAura1 = activityTripAuraRepo.findById(id).orElseThrow(() -> new RuntimeException("Activity Trip Aura  does not exist "));


        activityTripAura1.setName(activityTripAura.getName());
        activityTripAura1.setDateOfBegining(activityTripAura.getDateOfBegining());
        activityTripAura1.setDateOfEnding(activityTripAura.getDateOfEnding());
        activityTripAura1.setTypeActivity(activityTripAura.getTypeActivity());
        activityTripAura1.setPrice(activityTripAura.getPrice());
        return activityTripAuraRepo.save(activityTripAura1);

    }

    @Override
    public void deleteActivityTripAurasById(String id) {
        if (activityTripAuraRepo.findById(id).isPresent()) {
            activityTripAuraRepo.deleteById(id);
        }  else {
        throw new CustomException("There is no activity trip aura with that ID");
    }
    }
    public List<ActivityTripAura> getActivitiesByType(TypeActivity typeActivity) {
        return activityTripAuraRepo.findByTypeActivity(typeActivity);
    }





}
