package tn.talan.tripaura_backend.dto.stripe;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StripeResponse {

    private String intentId;
    private String clientSecret;
}
