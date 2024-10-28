package tn.talan.tripaura_backend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tn.talan.tripaura_backend.entities.RoleType;
import tn.talan.tripaura_backend.entities.UserTripAura;
import tn.talan.tripaura_backend.exceptions.CustomException;
import tn.talan.tripaura_backend.dto.LoginRequest;
import tn.talan.tripaura_backend.dto.LoginResponse;
import tn.talan.tripaura_backend.dto.UserTripAuraDTO;
import tn.talan.tripaura_backend.security.JwtService;
import tn.talan.tripaura_backend.services.Session.SessionService;
import tn.talan.tripaura_backend.services.UserTripAuraService;

import java.util.Collections;
import java.util.List;
import java.util.Map;


@RestController
@Validated
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "ForumVote")
@RequestMapping("/TripAuraUsers")

public class UserTripAuraController {
    private final UserTripAuraService userTripAuraService;
    private final JwtService jwtService;
    private final SessionService sessionService;

    @Operation(description = "AddTripAuraUser")
    @PostMapping("/Add/{roleType}")

    public ResponseEntity<UserTripAura> AddTripAuraUser(@Valid @RequestBody  UserTripAura userTripAura,@PathVariable RoleType roleType) {
        UserTripAura addedUserTripAura = userTripAuraService.addUserTripAura(userTripAura,roleType);
        return new ResponseEntity<>(addedUserTripAura,HttpStatus.CREATED);
     }
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<String> handleCustomException(CustomException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginRequest loginUserDto, HttpServletResponse response) {
        UserTripAura authenticatedUser = userTripAuraService.authenticate(loginUserDto);
        String jwtToken = jwtService.generateToken(authenticatedUser);
        // Convertir UserTripAura en UserTripAuraDTO
        UserTripAuraDTO userDto = userTripAuraService.toDto(authenticatedUser);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setExpiresIn(jwtService.getExpirationTime());
        loginResponse.setUserDto(userDto); // Utiliser UserTripAuraDTO

        response.setHeader("Authorization", "Bearer " + jwtToken);

        return ResponseEntity.ok(loginResponse);
    }


    @GetMapping("/getUserBymail/{email}")
    public UserTripAura getRoomByAccommodationAndAavailablity(@PathVariable String email) {
        return userTripAuraService.findUserByEmail(email) ;
    }


    @GetMapping("/current")
    public ResponseEntity<UserTripAuraDTO> getCurrentUser() {
        UserTripAuraDTO currentUser = sessionService.getCurrentUser();
        return ResponseEntity.ok(currentUser);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        // Trouver l'utilisateur par email
        UserTripAura user = userTripAuraService.findUserByEmail(email);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", "User not found"));
        }
        userTripAuraService.logoutUser(email);

        // Retourne une réponse JSON avec un message de succès
        return ResponseEntity.ok(Collections.singletonMap("message", "User logged out successfully"));
    }


}




