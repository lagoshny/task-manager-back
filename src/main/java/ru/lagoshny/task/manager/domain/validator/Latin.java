package ru.lagoshny.task.manager.domain.validator;

import ru.lagoshny.task.manager.domain.validator.impl.LatinValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Validator description {@link LatinValidator}.
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {LatinValidator.class})
public @interface Latin {

    String message() default "{common.validation.latin.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
