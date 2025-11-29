package ru.lagoshny.task.manager.domain.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.lagoshny.task.manager.domain.validator.impl.NotFeatureDateValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Validator description {@link NotFeatureDateValidator}.
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {NotFeatureDateValidator.class})
public @interface NotFeatureDate {

    String message() default "{common.validation.notFeature.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
