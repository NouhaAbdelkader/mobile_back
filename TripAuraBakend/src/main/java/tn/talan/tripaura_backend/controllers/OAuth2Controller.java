package tn.talan.tripaura_backend.controllers;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import tn.talan.tripaura_backend.entities.UserTripAura;
import tn.talan.tripaura_backend.repositories.UserTripAuraRepo;
import tn.talan.tripaura_backend.security.JwtService;
import tn.talan.tripaura_backend.services.Oauth2Service;
import tn.talan.tripaura_backend.services.UserTripAuraService;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
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
    private UserTripAuraRepo userTripAurarepo ;

    /*@GetMapping("/loginSuccess")
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
    }*/



    @GetMapping("/loginFailure")
    public ResponseEntity<Map<String, Object>> loginFailure() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Login failed");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
    @PostMapping("loginSuccess2")
    public ResponseEntity<String> loginSuccess(@RequestBody Map<String, String> payload) {
        String idToken = payload.get("idToken");

        if (idToken == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token Google manquant dans la requête");
        }

        // Vérification du token Google avec Google API
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
                .setAudience(Collections.singletonList("381084946378-t50vsol6ulce4mn6a4rvgugttki2vtoh.apps.googleusercontent.com"))
                .build();

        try {
            GoogleIdToken googleIdToken = verifier.verify(idToken);
            if (googleIdToken == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token Google invalide");
            }

            // Récupère l'email de l'utilisateur
            String email = googleIdToken.getPayload().getEmail();

            // Recherche ou création de l'utilisateur dans la base de données
            UserTripAura user = userTripAurarepo.findUserTripAuraByEmail(email);
            if (user == null) {
                user = new UserTripAura();
                user.setEmail(email);
                userTripAurarepo.save(user);
            }

            // Génération du token JWT pour l'utilisateur
            String jwtToken = jwtService.generateToken(user);

            // Retourne le token JWT dans le corps de la réponse
            Map<String, String> response = new HashMap<>();
            response.put("token", jwtToken);

            return ResponseEntity.ok(new JSONObject(response).toString());

        } catch (GeneralSecurityException | IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la vérification du token Google : " + e.getMessage());
        }
    }



   /*@GetMapping("/loginSuccess2")
   public ResponseEntity<?> loginSuccess2(@RequestHeader("Authorization") String authHeader) {
       try {
           // Extraire le token Google du header
           String googleToken = authHeader.replace("Bearer ", "");

           // Vérifier le token Google
           GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                   .setAudience(Collections.singletonList("YOUR_CLIENT_ID")) // Mettre votre Client ID Google
                   .build();

           GoogleIdToken idToken = verifier.verify(googleToken);
           if (idToken != null) {
               GoogleIdToken.Payload payload = idToken.getPayload();
               String email = payload.getEmail();

               // Créer ou récupérer l'utilisateur
               UserTripAura user = userTripAuraService.findUserByEmail(email);
               if (user == null) {
                   // Créer un nouvel utilisateur si nécessaire
                   user = new UserTripAura();
                   user.setEmail(email);
                   // Définir d'autres propriétés
                   userTripAurarepo.save(user);
               }

               // Générer le JWT
               String jwtToken = jwtService.generateToken(user);

               // Retourner le token dans le body
               Map<String, String> response = new HashMap<>();
               response.put("token", jwtToken);
               return ResponseEntity.ok(response);
           }

           return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
       } catch (Exception e) {
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
       }
   }*/

}