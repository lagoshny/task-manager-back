package ru.lagoshny.task.manager.domain.validator.impl;


import ru.lagoshny.task.manager.utils.StringUtils;
import ru.lagoshny.task.manager.domain.validator.Email;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Custom JSR validator to validate email address.
 * To validate email using apache {@link org.apache.commons.validator.routines.EmailValidator}.
 */
public class EmailValidator implements ConstraintValidator<Email, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isBlank(value)) {
            return true;
        }
        return org.apache.commons.validator.routines.EmailValidator.getInstance().isValid(value);
    }

}
