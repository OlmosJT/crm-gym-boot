package com.epam.crmgymboot.exception;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorAPI> handleEntityNotFoundException(
            EntityNotFoundException exception,
            WebRequest request
    ) {
        ErrorAPI body = new ErrorAPI(
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now(),
                exception.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<ErrorAPI> handleEntityExistException(
            EntityExistsException exception,
            WebRequest request
    ) {
        ErrorAPI body = new ErrorAPI(
                HttpStatus.CONFLICT.value(),
                LocalDateTime.now(),
                exception.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorAPI> handleMethodArgsNotValidException(
            MethodArgumentNotValidException exception,
            WebRequest request
    ) {
        Map<String, String> argumentErrors = new HashMap<>();

        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();

            argumentErrors.put(fieldName, message);
        });

        ErrorAPI body = new ErrorAPI(
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                exception.getMessage() + ":" + argumentErrors.entrySet(),
                request.getDescription(false));

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorAPI> handleConstraintViolationException(
            ConstraintViolationException e,
            WebRequest request
    ) {
        List<String> errors = new ArrayList<>();
        for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
            errors.add(violation.getMessage());
        }

        return new ResponseEntity<>(new ErrorAPI(
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                errors.toString(),
                request.getDescription(false)
        ), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorAPI> handleUsernameNotFoundException(
            UsernameNotFoundException exception,
            WebRequest request
    ) {
        return new ResponseEntity<>(new ErrorAPI(
                HttpStatus.UNAUTHORIZED.value(),
                LocalDateTime.now(),
                exception.getMessage(),
                request.getDescription(false)
        ), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ErrorAPI> handleLockedException(
            LockedException exception,
            WebRequest request
    ) {
        return new ResponseEntity<>(new ErrorAPI(
                HttpStatus.LOCKED.value(),
                LocalDateTime.now(),
                exception.getMessage(),
                request.getDescription(false)
        ), HttpStatus.LOCKED);
    }
}
