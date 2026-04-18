package com.bankcards.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // The HTTP 404 Not Found client error response
    @ExceptionHandler(CardNotFoundException.class)
    public ResponseEntity<String> handleNotFound(CardNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    // The HTTP 403 Forbidden client error response
    @ExceptionHandler(CardAccessDeniedException.class)
    public ResponseEntity<String> handleAccessDenied(CardAccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ex.getMessage());
    }

    // The HTTP 400 Bad Request client error response
    @ExceptionHandler(InvalidTransferException.class)
    public ResponseEntity<String> handleBadRequest(InvalidTransferException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }
}
