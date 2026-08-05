package com.instantwin.bank.utilities.Transaction;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class TransactionExeptionHandlerTest {

    private static final String ERROR_MESSAGE = "Test error";

    private TransactionExeptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new TransactionExeptionHandler();
    }

    @Test
    void testHandleUserIdInputFormatInvalidException_returns_bad_request_with_exception_message() {
        var result = exceptionHandler.handleUserIdInputFormatInvalidException(
                new UserIdInputFormatInvalidException(ERROR_MESSAGE));

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals(ERROR_MESSAGE, result.getBody());
    }

    @Test
    void testHandleIllegalArgumentException_returns_bad_request_with_exception_message() {
        var result = exceptionHandler.handleIllegalArgumentException(
                new IllegalArgumentException(ERROR_MESSAGE));

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals(ERROR_MESSAGE, result.getBody());
    }
}
