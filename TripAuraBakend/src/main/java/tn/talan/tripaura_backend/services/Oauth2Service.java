package tn.talan.tripaura_backend.services;

import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import tn.talan.tripaura_backend.entities.UserTripAura;
import tn.talan.tripaura_backend.repositories.UserTripAuraRepo;

@Service
@AllArgsConstructor

public class Oauth2Service {
    UserTripAuraRepo userTripAuraRepo ;
    public void saveOidcUserDetails(OidcUser oidcUser) {
        String email = oidcUser.getEmail();
        UserTripAura existingUser = userTripAuraRepo.findUserTripAuraByEmail(email);

        if (existingUser == null) {
            UserTripAura newUser = new UserTripAura();
            newUser.setEmail(email);
            newUser.setFirstName(oidcUser.getGivenName());
            newUser.setLastName(oidcUser.getFamilyName());
            // Ajoutez d'autres détails utilisateur si nécessaire

            userTripAuraRepo.save(newUser);
        }
    }

    public void saveOAuth2UserDetails(OAuth2User oauth2User) {
        String email = oauth2User.getAttribute("email");
        UserTripAura existingUser = userTripAuraRepo.findUserTripAuraByEmail(email);

        if (existingUser == null) {
            UserTripAura newUser = new UserTripAura();
            newUser.setEmail(email);
            newUser.setFirstName(oauth2User.getAttribute("given_name"));
            newUser.setLastName(oauth2User.getAttribute("family_name"));
            // Ajoutez d'autres détails utilisateur si nécessaire

            userTripAuraRepo.save(newUser);
        }
    }
}