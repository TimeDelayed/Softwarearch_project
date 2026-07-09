package com.instantwin.slotmachine.utilities;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SlotErrorHandler {
    @ExceptionHandler(GameRulesUnavailableException.class)
    public ResponseEntity<String> handleGameRulesUnavailable(
            GameRulesUnavailableException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ex.getMessage());
    }

    @ExceptionHandler(BetAmountInvalidException.class)
    public ResponseEntity<String> handleBetAmountInvalid(
            BetAmountInvalidException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    @ExceptionHandler(InvalidSlotProbabilities.class)
    public ResponseEntity<String> handleInvalidSlotProbabilities(
            InvalidSlotProbabilities ex
    ) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ex.getMessage());
    }

    @ExceptionHandler(ModelValidityBreachException.class)
    public ResponseEntity<String> handleModelValidityBreached(
            ModelValidityBreachException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    

}
