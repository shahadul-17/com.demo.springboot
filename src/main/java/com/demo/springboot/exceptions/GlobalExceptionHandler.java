package com.demo.springboot.exceptions;

import com.demo.springboot.core.MapBuilder;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleException(final Exception exception) {
        final var responseBody = MapBuilder.create(String.class, Object.class)
                .put("status", HttpStatus.INTERNAL_SERVER_ERROR.value())
                .put("message", exception.getMessage())
                .build();

        if (exception instanceof ObjectOptimisticLockingFailureException optimisticLockingFailureException) {
            responseBody.put("status", HttpStatus.BAD_REQUEST.value());
        } else if (exception instanceof ResponseStatusException responseStatusException) {
            responseBody.put("status", responseStatusException.getStatusCode().value());
            responseBody.put("message", responseStatusException.getReason());
        } else if (exception instanceof DataIntegrityViolationException dataIntegrityViolationException) {
            // this needs to be checked thoroughly...
            if (dataIntegrityViolationException.getCause() instanceof ConstraintViolationException constraintViolationException) {
                responseBody.put("status", HttpStatus.UNPROCESSABLE_ENTITY.value());
                // responseBody.put("message", "Provided " + constraintName + " is already in use.");
                responseBody.put("message", constraintViolationException.getMessage());
            }
        }

        return ResponseEntity
                .status((int) responseBody.get("status"))
                .body(responseBody);
    }

    /*@ExceptionHandler({ObjectOptimisticLockingFailureException.class})
    public ResponseEntity<Object> handleStudentNotFoundException(final ObjectOptimisticLockingFailureException exception) {
        final Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("status", HttpStatus.BAD_REQUEST.value());
        responseBody.put("message", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(responseBody);
    }*/

}
