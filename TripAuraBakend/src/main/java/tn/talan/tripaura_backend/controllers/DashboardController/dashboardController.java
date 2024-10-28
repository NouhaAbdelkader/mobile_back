package tn.talan.tripaura_backend.controllers.DashboardController;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.talan.tripaura_backend.entities.Gender;
import tn.talan.tripaura_backend.entities.RoleType;
import tn.talan.tripaura_backend.entities.UserTripAura;
import tn.talan.tripaura_backend.services.Dashboard.IDashboard;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/dashboard")
@AllArgsConstructor
public class dashboardController {

    private final IDashboard dashboardService;

    // ***************************** Accounts management *******************************
    @PostMapping("/users/add/{roleType}")
    public ResponseEntity<UserTripAura> addUserTripAura(@Valid  @RequestBody UserTripAura userTripAura, @PathVariable RoleType roleType) {
        UserTripAura createdUser = dashboardService.addUserTripAura(userTripAura, roleType);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @GetMapping("/users/all")
    public ResponseEntity<List<UserTripAura>> getUsersTripAura() {
        List<UserTripAura> users = dashboardService.getUsersTripAura();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/users/update")
    public ResponseEntity<UserTripAura> updateUserTripAura(@RequestBody UserTripAura userTripAura) {
        UserTripAura updatedUser = dashboardService.updateuserTripAura(userTripAura);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/users/delete/{id}")
    public ResponseEntity<Void> deleteUserTripAura(@PathVariable String id) {
        dashboardService.deleteUserTripAura(id);
        return ResponseEntity.noContent().build();
    }


    @Operation(description = "Search for users")
    @GetMapping("/users/search")
    public ResponseEntity<List<UserTripAura>> searchAirline(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String firstName,
            @RequestParam (required = false)  String lastName,
            @RequestParam (required = false)  String cin,
            @RequestParam (required = false)  String number,
            @RequestParam (required = false)  Gender gender,
            @RequestParam (required = false) Set<RoleType> roles)


    {
        System.out.println("Received Params - email: " + email + ", firstName: " + firstName + ", lastName: " + lastName
                + ", cin: " + cin + ", number: " + number + ", gender: " + gender + ", roles: " + roles);

        List<UserTripAura> userTripAuras = dashboardService.searchUsersTripAura( email,  firstName,  lastName,  cin,  number,  gender, roles) ;

        return ResponseEntity.ok(userTripAuras);
    }


    @Operation(description = "Get all users with agent roles")
    @GetMapping("/users/agents")
    public ResponseEntity<List<UserTripAura>> getAgents() {
        List<UserTripAura> agents = dashboardService.searchAgents();
        return new ResponseEntity<>(agents, HttpStatus.OK);
    }

    @Operation(description = "Get all users with traveller roles")
    @GetMapping("/users/travellers")
    public ResponseEntity<List<UserTripAura>> getTravellers() {
        List<UserTripAura> agents = dashboardService.searchTravellers();
        return new ResponseEntity<>(agents, HttpStatus.OK);
    }
    @Operation(description = "Get all users with agent roles")
    @GetMapping("/users/companies")
    public ResponseEntity<List<UserTripAura>> getCompanies() {
        List<UserTripAura> agents = dashboardService.searchCompanies();
        return new ResponseEntity<>(agents, HttpStatus.OK);
    }


    @GetMapping("/users/activeAgents")
    public ResponseEntity<List<UserTripAura>> getActiveAgents() {
        List<UserTripAura> activeAgents = dashboardService.getActiveAgents();
        return ResponseEntity.ok(activeAgents);
    }


    @GetMapping("/users/new/agents")
    public ResponseEntity<List<String>> getNewAgentsThisMonth() {
        List<String> newAgents = dashboardService.getNewAgentsThisMonth();
        return ResponseEntity.ok(newAgents);
    }

    @GetMapping("/users/activeTravellers")
    public ResponseEntity<List<UserTripAura>> getActiveTravellers() {
        List<UserTripAura> activeTravellers = dashboardService.getActiveTravellers();
        return ResponseEntity.ok(activeTravellers);
    }

    @GetMapping("/users/new/travellers")
    public ResponseEntity<List<String>> getNewTravellersThisMonth() {
        List<String> newTravellers = dashboardService.getNewTravellersThisMonth();
        return ResponseEntity.ok(newTravellers);
    }

    @GetMapping("/users/activeCompanies")
    public ResponseEntity<List<UserTripAura>> getActiveCompanies() {
        List<UserTripAura> activeCompanies = dashboardService.getActiveCompanies();
        return ResponseEntity.ok(activeCompanies);
    }

    @GetMapping("/users/new/companies")
    public ResponseEntity<List<String>> getNewCompaniesThisMonth() {
        List<String> newCompanies = dashboardService.getNewCompaniesThisMonth();
        return ResponseEntity.ok(newCompanies);
    }

    @PutMapping("/users/changePassword")
    public ResponseEntity<UserTripAura> changePassword(@RequestBody UserTripAura user) {
        try {
            UserTripAura updatedUser = dashboardService.changePassword(user);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @PostMapping("/users/verify-password")
    public ResponseEntity<Boolean> verifyPassword(@RequestBody UserTripAura userTripAura, @RequestParam String currentPassword) {
        // Call the service method to verify the password
        Boolean isPasswordValid = dashboardService.verifPassword(userTripAura, currentPassword);

        // Return the result of the verification
        return ResponseEntity.ok(isPasswordValid);
    }



    // ***************************** Flights management *******************************


    // ***************************** Airlines management *******************************


    // ***************************** Activities management *******************************


    // ***************************** Accommodations management *******************************
}
