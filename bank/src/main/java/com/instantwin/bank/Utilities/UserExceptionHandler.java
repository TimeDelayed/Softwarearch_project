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

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<String> handleNegativeBalanceException(InsufficientBalanceException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(TransactionNumberInvalidException.class)
    public ResponseEntity<String> handleTransactionNumberInvalidException(TransactionNumberInvalidException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(DecimalPlaceInvalidException.class)
    public ResponseEntity<String> handleDecimalPlaceInvalidException(DecimalPlaceInvalidException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

}
