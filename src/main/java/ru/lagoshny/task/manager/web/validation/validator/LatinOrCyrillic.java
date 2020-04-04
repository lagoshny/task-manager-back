package ru.lagoshny.task.manager.web.validation.validator;

import org.hibernate.validator.constraints.ConstraintComposition;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.hibernate.validator.constraints.CompositionType.OR;

/**
 * Combined validator from several small validators checks that
 * value can contains only Latin {@link Latin} or Cyrillic {@link Cyrillic}.
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = { })
@ConstraintComposition(OR)
@ReportAsSingleViolation
@Latin
@Cyrillic
public @interface LatinOrCyrillic {

    String message() default "{combined.validation.latinOrCyrillic.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
