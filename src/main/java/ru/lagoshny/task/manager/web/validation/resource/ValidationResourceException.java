package ru.lagoshny.task.manager.web.validation.resource;

import org.jetbrains.annotations.NotNull;
import org.springframework.validation.FieldError;

import java.util.List;

/**
 * Throws when {@link org.springframework.hateoas.Resource} doesn't pass validation.
 */
public class ValidationResourceException extends RuntimeException {

    private List<FieldError> errors;

    public ValidationResourceException(@NotNull List<FieldError> errors) {
        this.errors = errors;
    }

    public ValidationResourceException(@NotNull List<FieldError> errors, Throwable cause) {
        super(cause);
        this.errors = errors;
    }

    public List<FieldError> getErrors() {
        return errors;
    }

}
