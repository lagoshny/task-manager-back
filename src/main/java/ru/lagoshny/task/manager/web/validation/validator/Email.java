package ru.lagoshny.task.manager.web.validation.validator;


import ru.lagoshny.task.manager.web.validation.validator.impl.EmailValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
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
