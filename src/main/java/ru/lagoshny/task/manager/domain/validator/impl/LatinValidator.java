package ru.lagoshny.task.manager.domain.validator.impl;


import ru.lagoshny.task.manager.utils.StringUtils;
import ru.lagoshny.task.manager.domain.validator.Latin;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

/**
 * Checks that if the string is not blank then it needs to contains only Latin symbols.
 */
public class LatinValidator implements ConstraintValidator<Latin, String> {

    private Pattern pattern = compile("^[a-zA-Z]+$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isBlank(value)) {
            return true;
        }

        final Matcher m = pattern.matcher(value);
        return m.matches();
    }

}
