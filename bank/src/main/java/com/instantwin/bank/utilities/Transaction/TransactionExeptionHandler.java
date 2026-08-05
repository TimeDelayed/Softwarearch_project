package com.instantwin.bank.utilities.Transaction;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TransactionExeptionHandler {
    

    @ExceptionHandler(UserIdInputFormatInvalidException.class)
    public ResponseEntity<String> handleUserIdInputFormatInvalidException(UserIdInputFormatInvalidException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
