package ru.lagoshny.task.manager.web.validation.validator.impl;


import ru.lagoshny.task.manager.web.validation.validator.NotFeatureDate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

/**
 * Checks that passed date value less or equal current date without time.
 */
public class NotFeatureDateValidator implements ConstraintValidator<NotFeatureDate, LocalDate> {

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        final LocalDate now = LocalDate.now();
        return now.isAfter(value) || now.isEqual(value);
    }

}
