package tn.talan.tripaura_backend.helpers;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = PasswordConstraintsValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {

    String message() default "Invalid password!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
