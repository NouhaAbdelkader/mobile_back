package tn.talan.tripaura_backend.dto;

import lombok.Getter;
import lombok.Setter;
import tn.talan.tripaura_backend.entities.RoleType;

import java.util.Set;

@Getter
@Setter
public class UserTripAuraDTO {

    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Set<RoleType> roles;
    // Autres champs nécessaires

    // Getters et Setters


}

