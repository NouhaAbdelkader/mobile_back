package tn.talan.tripaura_backend.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.talan.tripaura_backend.entities.UserTripAura;
import tn.talan.tripaura_backend.security.JwtService;
import tn.talan.tripaura_backend.services.Oauth2Service;
import tn.talan.tripaura_backend.services.UserTripAuraService;
import java.util.HashMap;
import java.util.Map;

@RestController
@Tag(name = "auth")
@AllArgsConstructor
@RequestMapping("/auth")
public class OAuth2Controller {
    private final Oauth2Service oauth2Service;
    private final JwtService jwtService;
    private final UserTripAuraService userTripAuraService;

    @GetMapping("/loginSuccess")
    public ResponseEntity<Void> loginSuccess(HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String userEmail = null;
        String token = null;
        System.out.println("???? : " + userEmail);

        if (principal instanceof OidcUser) {
            OidcUser oidcUser = (OidcUser) principal;
            oauth2Service.saveOidcUserDetails(oidcUser);
            userEmail = oidcUser.getEmail();
            System.out.println("oidcUser : " + userEmail + " token : " + token);

        } else if (principal instanceof OAuth2User) {
            OAuth2User oauth2User = (OAuth2User) principal;
            oauth2Service.saveOAuth2UserDetails(oauth2User);
            userEmail = oauth2User.getAttribute("email");
            System.out.println("oauth2User : " + userEmail + " token : " + token);

        }

        if (userEmail != null) {
            UserTripAura authenticatedUser = userTripAuraService.findUserByEmail(userEmail);
            if (authenticatedUser != null) {
                token = jwtService.generateToken(authenticatedUser);

                response.setHeader("Authorization", "Bearer " + token);
                System.out.println("user : " + userEmail + " token : " + token);
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/loginFailure")
    public ResponseEntity<Map<String, Object>> loginFailure() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Login failed");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
}