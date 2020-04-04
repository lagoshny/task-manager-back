package ru.lagoshny.task.manager.web.validation.validator;


import ru.lagoshny.task.manager.web.validation.validator.impl.CyrillicValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Validator description {@link CyrillicValidator}.
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {CyrillicValidator.class})
public @interface Cyrillic {

    String message() default "{common.validation.cyrillic.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
