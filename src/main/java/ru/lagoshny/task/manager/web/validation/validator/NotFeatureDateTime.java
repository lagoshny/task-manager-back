package ru.lagoshny.task.manager.web.validation.validator;

import ru.lagoshny.task.manager.web.validation.validator.impl.NotFeatureDateTimeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Validator description {@link NotFeatureDateTimeValidator}.
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {NotFeatureDateTimeValidator.class})
public @interface NotFeatureDateTime {

    String message() default "{common.validation.notFeature.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
