package tn.talan.tripaura_backend.services.Session;

import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import tn.talan.tripaura_backend.dto.UserTripAuraDTO;
import tn.talan.tripaura_backend.entities.UserTripAura;
import tn.talan.tripaura_backend.exceptions.CustomException;
import tn.talan.tripaura_backend.repositories.UserTripAuraRepo;

@Service
@AllArgsConstructor
public class SessionService {

    private final UserTripAuraRepo userTripAuraRepo;

    public UserTripAuraDTO getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String email = ((UserDetails) principal).getUsername();
            UserTripAura user = userTripAuraRepo.findUserTripAuraByEmail(email);
            if (user == null) {
                throw new CustomException("User not found");
            }
            return toDto(user);
        }
        throw new CustomException("Authentication information is not available");
    }

    private UserTripAuraDTO toDto(UserTripAura user) {
        UserTripAuraDTO dto = new UserTripAuraDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setPhoneNumber(user.getNumber());
        dto.setRoles(user.getRoles());
        return dto;
    }
}