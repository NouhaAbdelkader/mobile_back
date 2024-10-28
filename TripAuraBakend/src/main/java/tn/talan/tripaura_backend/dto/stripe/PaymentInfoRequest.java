package tn.talan.tripaura_backend.dto.stripe;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class PaymentInfoRequest {

    private int amount;
    private String currency;
    private String receiptEmail;

}
