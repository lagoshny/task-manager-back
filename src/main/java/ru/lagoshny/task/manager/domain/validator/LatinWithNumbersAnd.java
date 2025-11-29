package ru.lagoshny.task.manager.domain.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.lagoshny.task.manager.domain.validator.impl.LatinWithNumbersAndValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Validator description {@link LatinWithNumbersAndValidator}.
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {LatinWithNumbersAndValidator.class})
public @interface LatinWithNumbersAnd {

    String message() default "{combined.validation.latinWithNumberAnd.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String[] additionalSymbols() default {};

}
