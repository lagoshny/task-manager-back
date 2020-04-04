package ru.lagoshny.task.manager.web.validation.validator.impl;


import ru.lagoshny.task.manager.utils.StringUtils;
import ru.lagoshny.task.manager.web.validation.validator.Cyrillic;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

/**
 * Checks that if the string is not blank then it needs to contains only Cyrillic symbols.
 */
public class CyrillicValidator implements ConstraintValidator<Cyrillic, String> {

    private Pattern pattern = compile("^[а-яёЁА-Я]+$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isBlank(value)) {
            return true;
        }

        final Matcher m = pattern.matcher(value);
        return m.matches();
    }

}
