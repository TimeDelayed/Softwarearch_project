package com.instantwin.bank.Utilities;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler(ModelValidityBreachException.class)
    public ResponseEntity<String> handleModelValidityBreachException(ModelValidityBreachException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

}
