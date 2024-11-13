package tn.talan.tripaura_backend.services;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.talan.tripaura_backend.entities.RoleType;
import tn.talan.tripaura_backend.entities.UserTripAura;
import tn.talan.tripaura_backend.exceptions.CustomException;
import tn.talan.tripaura_backend.repositories.UserTripAuraRepo;
import tn.talan.tripaura_backend.dto.LoginRequest;
import tn.talan.tripaura_backend.dto.UserTripAuraDTO;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.HashSet;
@Service
@AllArgsConstructor
public class UserTripAuraImp implements UserTripAuraService {
    private final UserTripAuraRepo userTripAuraRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    public UserTripAura addUserTripAura(UserTripAura userTripAura, RoleType roleType) {
        if (userTripAuraRepo.existsUserTripAurasByEmail(userTripAura.getEmail())) {
            throw new CustomException("there is already a user trip Aura with this email");
        }

        //  if (userTripAuraRepo.existsUserTripAuraByNumber(userTripAura.getNumber()) || userTripAura.getNumber().length() != 8) {
        //    throw new CustomException("there is already a user trip Aura with this number");
        // }
        else {
            String encodedPassword = passwordEncoder.encode(userTripAura.getPassword());
            userTripAura.setPassword(encodedPassword);
            userTripAura.setRoles(new HashSet<>(Collections.singleton(roleType)));
            return userTripAuraRepo.save(userTripAura);
        }
    }

    @Override
    public UserTripAura authenticate(LoginRequest input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        UserTripAura user = userTripAuraRepo.findUserTripAuraByEmail(input.getEmail());

        // Mettre à jour l'heure de connexion
        user.setLoginTime(LocalDateTime.now());

        // Enregistrer les changements dans la base de données
        saveUser(user);


        return userTripAuraRepo.findUserTripAuraByEmail(input.getEmail());
    }
    @Override
    public void logoutUser(String email) {
        UserTripAura user =  userTripAuraRepo.findUserTripAuraByEmail(email);
        if (user != null) {
            System.out.println(user);
            handleLogout(user);
        } else {
            System.out.println("User not found during logout.");
        }
    }

    public void handleLogout(UserTripAura user) {
        try {
            if (user.getLoginTime() != null) {
                // Calculer la durée de la session en minutes
                LocalDateTime loginTime = user.getLoginTime();
                long sessionDuration = Duration.between(loginTime, LocalDateTime.now()).toMinutes();
                System.out.println("Session duration: " + sessionDuration + " minutes");

                // Ajouter la durée de la session au temps total de connexion
                user.setTotalLoggedInTime(user.getTotalLoggedInTime() + sessionDuration);
                System.out.println("Total logged in time updated to: " + user.getTotalLoggedInTime());

                // Réinitialiser l'heure de connexion
                user.setLoginTime(null);

                // Enregistrer les changements dans la base de données
                userTripAuraRepo.save(user);
            } else {
                System.out.println("Login time is null during logout.");
            }
        } catch (DateTimeParseException e) {
            System.out.println("Exception during handleLogout: " + e.getMessage());
        }
    }

    @Override
    public UserTripAuraDTO toDto(UserTripAura user) {
        UserTripAuraDTO dto = new UserTripAuraDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setPhoneNumber(user.getNumber());
        dto.setRoles(user.getRoles());
        return dto;
    }

    @Override
    public UserTripAura findUserByEmail(String email) {
        return userTripAuraRepo.findUserTripAuraByEmail(email);
    }

    @Transactional
    public void saveUser(UserTripAura user) {
        userTripAuraRepo.save(user);
    }
}



