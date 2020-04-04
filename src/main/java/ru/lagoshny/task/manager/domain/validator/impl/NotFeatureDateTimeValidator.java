package ru.lagoshny.task.manager.domain.validator.impl;


import ru.lagoshny.task.manager.domain.validator.NotFeatureDateTime;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

/**
 * Checks that passed date value less or equal current date with time.
 */
public class NotFeatureDateTimeValidator implements ConstraintValidator<NotFeatureDateTime, LocalDateTime> {

    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        final LocalDateTime now = LocalDateTime.now();
        return now.isAfter(value) || now.isEqual(value);
    }

}
