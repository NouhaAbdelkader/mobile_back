package tn.talan.tripaura_backend.controllers.activities;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tn.talan.tripaura_backend.entities.activities.ActivityTripAura;
import tn.talan.tripaura_backend.exceptions.CustomException;
import tn.talan.tripaura_backend.repositories.activities.ActivityTripAuraRepo;
import tn.talan.tripaura_backend.services.activities.ActivityTripAuraImpl;
import tn.talan.tripaura_backend.services.activities.ActivityTripAuraService;

import java.util.List;
import java.util.Optional;


@RestController
@Validated
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/activitiyTripAura")

public class ActivityTripAuraController {

    private ActivityTripAuraImpl activityTripAuri;
    private ActivityTripAuraRepo activityTripAuraRepo;
    private ActivityTripAuraService activityTripAuraService;


    @Operation(description = "getTripAuraActivity")
    @GetMapping("/Get/TripAuraActivity")
    public List<ActivityTripAura> getAllActivities() {
        return activityTripAuraService.getAllActivitites();
    }
    @GetMapping("/get/TripAuraActivity/{id}")
    public Optional<ActivityTripAura> getActivityById(@PathVariable String id) {
        return activityTripAuraService.getActivityById(id);
    }

    @Operation(description = "addTripAuraActivity")
    @PostMapping ("/add/TripAuraActivity")
    public ActivityTripAura addActivity(@RequestBody ActivityTripAura activityTripAura) {
        return activityTripAuraService.addActivity(activityTripAura);
    }

    @Operation(description = "updateTripAuraActivityById")
    @PutMapping ("/update/ActivityTripAura/{id}")
    public ActivityTripAura updateActivity(@PathVariable String id, @RequestBody ActivityTripAura activityDetails) {
      
        activityTripAuraService.getActivityById(id);
        return activityTripAuraService.updateActivity(activityDetails,id);
    }

    @Operation(description = "deleteTripAuraActivityById")
    @DeleteMapping("/delete/ActivityTripAura/{id}")
    public void deleteActivity(@PathVariable String id) {
        activityTripAuraService.deleteActivityTripAurasById(id);
    }
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<String> handleCustomException(CustomException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }


}
