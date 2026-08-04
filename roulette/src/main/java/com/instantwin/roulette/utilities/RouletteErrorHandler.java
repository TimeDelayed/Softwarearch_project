package com.instantwin.roulette.utilities;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RouletteErrorHandler {

    @ExceptionHandler(InvalidBetException.class)
    public ResponseEntity<String> handleInvalidBetException(InvalidBetException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
