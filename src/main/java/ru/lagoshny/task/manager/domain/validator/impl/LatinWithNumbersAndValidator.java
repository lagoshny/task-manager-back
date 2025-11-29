package ru.lagoshny.task.manager.domain.validator.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.lagoshny.task.manager.domain.validator.LatinWithNumbersAnd;
import ru.lagoshny.task.manager.utils.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Checks that passed string value contains Latin characters, numbers and additional passed symbols.
 */
public class LatinWithNumbersAndValidator implements ConstraintValidator<LatinWithNumbersAnd, String> {

    private static final String LATIN_W_NUMBER_REG_EXP = "^[a-zA-Z0-9%s]+$";

    private List<String> additionalSymbols = new ArrayList<>();

    @Override
    public void initialize(LatinWithNumbersAnd constraintAnnotation) {
        if (constraintAnnotation.additionalSymbols().length == 0) {
            throw new IllegalArgumentException("You did not specify additional symbols, "
                    + "use LatinWithNumbers validator");
        }
        additionalSymbols = Arrays.asList(constraintAnnotation.additionalSymbols());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isBlank(value)) {
            return true;
        }

        String symbols = String.join("\\", additionalSymbols);
        final Pattern pattern = Pattern.compile(String.format(LATIN_W_NUMBER_REG_EXP, symbols));
        final Matcher matcher = pattern.matcher(value);

        return matcher.matches();
    }

}
