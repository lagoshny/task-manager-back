package ru.lagoshny.task.manager.web.validation;

import org.springframework.hateoas.EntityModel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker annotation to validate request param as {@link EntityModel#getContent()}.
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidResource {

    /**
     * Specify one or more validation groups to apply to the validation step
     * kicked off by this annotation.
     * <p>JSR-303 defines validation groups as custom annotations which an application declares
     * for the sole purpose of using them as type-safe group arguments, as implemented in
     * {@link org.springframework.validation.beanvalidation.SpringValidatorAdapter}.
     * <p>Other {@link org.springframework.validation.SmartValidator} implementations may
     * support class arguments in other ways as well.
     */
    Class<?>[] value() default {};

}
