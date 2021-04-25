package com.jruchel.mlrest.exceptions;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ControllerAdvice
public class ErrorHandler {

    private final HttpStatus RESPONSE_STATUS = HttpStatus.CONFLICT;

    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public ResponseEntity<String> constraintViolation(DataIntegrityViolationException ex) {
        Pattern pattern = Pattern.compile(".*Szczegóły:(.+)");
        Matcher matcher = pattern.matcher(ex.getCause().getCause().getMessage());
        if (matcher.matches()) {
            return new ResponseEntity<>(matcher.group(1), RESPONSE_STATUS);
        }
        return new ResponseEntity<>("Constraint violation, incorrect input data.", RESPONSE_STATUS);
    }

    @ExceptionHandler(value = MessagingException.class)
    public ResponseEntity<String> messagingException(MessagingException ex) {
        return new ResponseEntity<>(String.format("Messaging error: %s", ex.getMessage()), RESPONSE_STATUS);
    }

    @ExceptionHandler(value = IOException.class)
    public ResponseEntity<String> IOError(IOException ex) {
        return new ResponseEntity<>(ex.getMessage(), RESPONSE_STATUS);
    }

    @ExceptionHandler(value = ValidationException.class)
    public ResponseEntity<List<String>> validationError(ValidationException ex) {
        return new ResponseEntity<>(Arrays.asList(ex.getMessage().split("\n")), RESPONSE_STATUS);
    }

}
