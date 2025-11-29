package ru.lagoshny.task.manager.domain.validator.impl;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.lagoshny.task.manager.domain.validator.Password;
import ru.lagoshny.task.manager.utils.StringUtils;

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

    private final PasswordEncoder passwordEncoder;

    private int minLength;

    private int maxLength;

    @Autowired
    public PasswordValidator(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

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
