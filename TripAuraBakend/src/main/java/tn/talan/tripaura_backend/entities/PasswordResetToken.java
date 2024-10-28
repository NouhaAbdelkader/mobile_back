package tn.talan.tripaura_backend.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "ResetPassword")
public class PasswordResetToken {

    private static final int EXPIRATION = 60 * 24;

    @Id
    private String id;
    @Indexed
    private String token;

    @Indexed
    private String userTripAuraId; // Store UserTripAura's ID

    private Date expiryDate;

    public PasswordResetToken(String token, String userTripAuraId) {
        this.token = token;
        this.userTripAuraId = userTripAuraId;
        this.expiryDate = new Date(System.currentTimeMillis() + EXPIRATION * 60 * 1000);

    }
}
