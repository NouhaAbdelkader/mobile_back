package tn.talan.tripaura_backend.entities.payment;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "Payment")
@Entity
@Data
public class CheckoutPayment {

     @Id
     private Long id;

     @Indexed
     private String name;

     @Indexed
    private String currency;

     @Indexed
    private String successUrl;

    @Indexed
    private String cancelUrl;

    @Indexed
    private long amount;

    @Indexed
    private long quantity;
    @Indexed
    private String PaymentMethodId ;


}
