package ru.lagoshny.task.manager.web.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.core.RepositoryConstraintViolationException;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.lagoshny.task.manager.utils.StringUtils;

import javax.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.Date;

/**
 * Common exceptions handler which occurred during HTTP request processing.
 * <p>
 * Converts exceptions to a uniform format {@link ServerError} for sending to the client.
 */
@ControllerAdvice
public class ServerExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ServerExceptionHandler.class);

    @ExceptionHandler(value = {RuntimeException.class})
    protected ResponseEntity<Object> handleRuntimeException(
            RuntimeException ex, WebRequest request) {
        logger.error(ex.getMessage(), ex);

        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, request, ex.getMessage());
    }


    @ExceptionHandler(value = {ConstraintViolationException.class})
    protected ResponseEntity<Object> handleValidationException(
            ConstraintViolationException ex, WebRequest request) {
        logger.error(ex.getMessage(), ex);

        final String[] errMessages = ex.getConstraintViolations()
                .stream()
                .map(constraintViolation -> constraintViolation.getPropertyPath()
                        + StringUtils.Const.COLON
                        + StringUtils.SPACE
                        + constraintViolation.getMessage())
                .sorted()
                .toArray(String[]::new);

        return buildErrorResponse(HttpStatus.BAD_REQUEST, request, errMessages);

    }

    @ExceptionHandler(value = {RepositoryConstraintViolationException.class})
    protected ResponseEntity<Object> handleRepositoryValidationException(
            RepositoryConstraintViolationException ex, WebRequest request) {
        logger.error(ex.getMessage(), ex);

        final String[] errMessages = ex.getErrors().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField()
                        + StringUtils.Const.COLON
                        + StringUtils.SPACE
                        + fieldError.getDefaultMessage())
                .sorted()
                .toArray(String[]::new);

        return buildErrorResponse(HttpStatus.BAD_REQUEST, request, errMessages);

    }

    @ExceptionHandler(value = {ResourceNotFoundException.class})
    protected ResponseEntity<Object> handleNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {
        logger.error(ex.getMessage(), ex);

        return buildErrorResponse(HttpStatus.NOT_FOUND, request, ex.getMessage());
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        logger.error(ex.getMessage(), ex);

        return buildErrorResponse(HttpStatus.BAD_REQUEST, request,
                ex.getCause().getLocalizedMessage());
    }

    private ResponseEntity<Object> buildErrorResponse(HttpStatus httpStatus,
                                                      WebRequest request,
                                                      String... msg) {
        final ServerError serverError = ServerError.builder()
                .path(((ServletWebRequest) request).getRequest().getRequestURI())
                .messages(Arrays.asList(msg))
                .timestamp(new Date())
                .status(httpStatus)
                .build();

        return new ResponseEntity<>(serverError,
                new HttpHeaders(), serverError.getStatus());
    }

}
