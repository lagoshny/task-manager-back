package ru.lagoshny.task.manager.domain.validator;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.lagoshny.task.manager.domain.validator.impl.EmailValidator;

import java.lang.annotation.*;

/**
 * Validator description {@link EmailValidator}.
 */
@Documented
@Constraint(validatedBy = EmailValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Email {

    String message() default "{common.validation.email.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
