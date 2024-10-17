package com.demo.springboot.exceptions;

import com.demo.springboot.core.MapBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
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
