package com.instantwin.bank.utilities.User;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler(ModelValidityBreachException.class)
    public ResponseEntity<String> handleModelValidityBreachException(ModelValidityBreachException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(DecimalPlaceInvalidException.class)
    public ResponseEntity<String> handleDecimalPlaceInvalidException(DecimalPlaceInvalidException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(AmountNegativeException.class)
    public ResponseEntity<String> handleAmountNegativeException(AmountNegativeException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
