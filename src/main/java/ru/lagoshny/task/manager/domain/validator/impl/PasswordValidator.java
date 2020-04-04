package ru.lagoshny.task.manager.domain.validator.impl;


import ru.lagoshny.task.manager.utils.StringUtils;
import ru.lagoshny.task.manager.domain.validator.Password;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Checks password length, valid characters and password strength.
 */
public class PasswordValidator implements ConstraintValidator<Password, String> {

    /**
     * Password should contains: at least 8 characters and include numbers with
     * upper and lower case letters of the Latin alphabet.
     */
    private static final String PASSWORD_REG_EXP = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[a-z]).{%s,%s}$";

    private int minLength;

    private int maxLength;

    @Override
    public void initialize(Password passwordAnnotation) {
        this.minLength = passwordAnnotation.minLength();
        this.maxLength = passwordAnnotation.maxLength();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isBlank(value)) {
            return true;
        }
        final Pattern passwordPattern = Pattern.compile(String.format(PASSWORD_REG_EXP, minLength, maxLength));
        final Matcher matcher = passwordPattern.matcher(value);

        return matcher.matches();
    }

}
