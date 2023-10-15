package ru.dyusov.BonusService.controller;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ErrorController {
    private Logger logger = LoggerFactory.getLogger(ErrorController.class);

    private Map<String, String> validationErrors(List<FieldError> errors) {
        Map<String, String> validationErrors = new HashMap<>();
        errors.forEach(fieldError -> {
            validationErrors.put(
                    fieldError.getField(),
                    "Field has wrong value " +
                            fieldError.getRejectedValue() +
                            ": " + fieldError.getDefaultMessage());
        });
        return validationErrors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String badRequest(MethodArgumentNotValidException exception) {
        Map<String, String> errors = validationErrors(exception.getBindingResult().getFieldErrors());
        logger.info("Bad request '{}'", errors);
        return exception.getMessage();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public String notFound(EntityNotFoundException exception) {
        return exception.getMessage();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    public String serverError(RuntimeException exception) {
        logger.error("", exception);
        return exception.getMessage();
    }
}
