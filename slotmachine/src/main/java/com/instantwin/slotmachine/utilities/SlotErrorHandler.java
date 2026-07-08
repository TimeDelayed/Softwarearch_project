package com.instantwin.slotmachine.utilities;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SlotErrorHandler {
    // TODO: ALL
    @ExceptionHandler(GameRulesUnavailableException.class)
    public ResponseEntity<String> handleGameRulesUnavailable(
            GameRulesUnavailableException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ex.getMessage());
    }
}
