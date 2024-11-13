package tn.talan.tripaura_backend.services.activities;

import tn.talan.tripaura_backend.entities.activities.ActivityTripAura;
import tn.talan.tripaura_backend.entities.activities.TypeActivity;

import java.util.List;
import java.util.Optional;

public interface ActivityTripAuraService {


    public List<ActivityTripAura> getAllActivitites();
    public Optional<ActivityTripAura> getActivityById(String id);
    public ActivityTripAura addActivity(ActivityTripAura activityTripAura);
    public ActivityTripAura updateActivity(ActivityTripAura activityTripAura,String id);
    public void deleteActivityTripAurasById(String id);
    public List<ActivityTripAura> getActivitiesByType(TypeActivity typeActivity);

}
