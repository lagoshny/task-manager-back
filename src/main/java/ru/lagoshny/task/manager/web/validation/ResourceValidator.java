package ru.lagoshny.task.manager.web.validation;

import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

/**
 * Interface to manual control validation request parameters.
 * {@see RequestResourceValidationProcessor} JavaDoc.
 */
public interface ResourceValidator {

    /**
     * Validate object and return result as {@link BindingResult}.
     *
     * @param obj       object witch need to validate
     * @param validator validator to validate object
     * @return validation results as {@link BindingResult}
     */
    default BindingResult validate(Object obj, Validator validator) {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(obj, obj.getClass().getSimpleName());
        validator.validate(obj, bindingResult);

        return bindingResult;
    }

}
