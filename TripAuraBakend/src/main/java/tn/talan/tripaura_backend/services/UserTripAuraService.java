package tn.talan.tripaura_backend.services;

import tn.talan.tripaura_backend.entities.RoleType;
import tn.talan.tripaura_backend.entities.UserTripAura;
import tn.talan.tripaura_backend.dto.LoginRequest;
import tn.talan.tripaura_backend.dto.UserTripAuraDTO;


public interface UserTripAuraService  {
    UserTripAura addUserTripAura(UserTripAura userTripAura,RoleType roleType);
    public UserTripAura authenticate(LoginRequest input);
    public UserTripAuraDTO toDto(UserTripAura user);

    public UserTripAura findUserByEmail(String email) ;

    void logoutUser(String email) ;

}
