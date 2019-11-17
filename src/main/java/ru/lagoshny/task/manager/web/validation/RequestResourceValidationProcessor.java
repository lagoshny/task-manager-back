package ru.lagoshny.task.manager.web.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Objects;
import java.util.Set;

/**
 * Workaround class to force JSR-303 annotation validation work for controller method parameters.
 * Check the issue <a href="https://jira.spring.io/browse/DATAREST-593">DATAREST-593</a>
 * <p>
 * This controller works for validate {@link Resource#getContent()} passed through request parameter which
 * annotated with {@link ValidResource}.
 * And if {@link Resource#getContent()} doesn't valid {@link ValidationResourceException}
 * throws and ExceptionHandler return answer with list of {@link ObjectError}.
 * <p>
 * If you want control validation process for {@link Resource#getContent()} you need switch off
 * this controller advice doing next steps:
 * - Don't annotate request parameter with {@link ValidResource}.
 * - Implement your controller with {@link ResourceValidator} interface and manually invoke validate method.
 */
@ControllerAdvice
public class RequestResourceValidationProcessor extends RequestBodyAdviceAdapter {

    private final Validator validator;

    @Autowired
    public RequestResourceValidationProcessor(Validator validator) {
        this.validator = validator;
    }

    @Override
    public boolean supports(MethodParameter methodParameter,
                            Type type,
                            Class<? extends HttpMessageConverter<?>> aClass) {
        final Annotation[] parameterAnnotations = methodParameter.getParameterAnnotations();
        for (final Annotation annotation : parameterAnnotations) {
            if (annotation.annotationType().equals(ValidResource.class)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Object afterBodyRead(Object body,
                                HttpInputMessage inputMessage,
                                MethodParameter parameter,
                                Type targetType,
                                Class<? extends HttpMessageConverter<?>> converterType) {
        Class<?>[] validationGroups = null;
        if (parameter.hasParameterAnnotation(ValidResource.class)) {
            validationGroups = Objects.requireNonNull(parameter.getParameterAnnotation(ValidResource.class)).value();
        }
        if (!(body instanceof Resource)) {
            final Object obj = super.afterBodyRead(body, inputMessage, parameter, targetType, converterType);
            validateObject(obj, validationGroups);
            return obj;
        } else {
            final Resource<?> resource =
                    (Resource<?>) super.afterBodyRead(body, inputMessage, parameter, targetType, converterType);
            validateObject(resource.getContent(), validationGroups);
            return resource;
        }
    }

    private void validateObject(Object obj, Class<?>[] groups) {
        final Set<ConstraintViolation<Object>> result = validator.validate(obj, groups);
        if (!result.isEmpty()) {
            throw new ConstraintViolationException(result);
        }
    }

}
