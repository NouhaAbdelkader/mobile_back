package tn.talan.tripaura_backend.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import tn.talan.tripaura_backend.helpers.ValidPassword;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "UserTripAura")

public class UserTripAura implements UserDetails, Serializable{
    @Id
    private String id;

    @NotEmpty(message = "firstName cannot be empty")
    @Indexed
    private String firstName;

    @Indexed
    @NotEmpty(message = "lastName cannot be empty")
    private String lastName;

    @Email(message = "Email is not valid", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    @NotEmpty(message = "Email cannot be empty")
    @Indexed
    private String email;

    //@Transient // meaning it will not be saved in DB
    //@Size(min = 3, max = 10, message = "{register.password.size}")
    @NotNull
    @ValidPassword
    @NotEmpty(message = "password cannot be empty")
    @Indexed
    private String password;

    @Indexed
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dateOfBirth;

    @Indexed
    private String cin;

    @Indexed
    @NotBlank(message = "Phone number is mandatory")
    @Pattern(regexp = "\\d{8}", message = "Phone number must be 8 digits")
    private String number;


    @ElementCollection(fetch = FetchType.EAGER)
    @Indexed
    private Set<RoleType> roles;


    @Indexed
    private Gender gender ;

    @Indexed
    @CreatedDate
    private LocalDateTime createdDate;

    @Indexed
    private long totalLoggedInTime= 0; // En minutes ou heures

    @Indexed
    private LocalDateTime loginTime; // Ajoutez ce champ pour capturer l'heure de connexion

  /*  @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }*/

    private String fcmToken; // Add this field to store the FCM token

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toList());
    }


    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }



}
