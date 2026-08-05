package com.instantwin.roulette.utilities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class RouletteErrorHandlerTest {

    private static final String ERROR_MESSAGE = "Test error";

    private RouletteErrorHandler errorHandler;

    @BeforeEach
    void setUp() {
        errorHandler = new RouletteErrorHandler();
    }

    @Test
    void testHandleInvalidBetException_returns_bad_request_with_exception_message() {
        var result = errorHandler.handleInvalidBetException(new InvalidBetException(ERROR_MESSAGE));

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals(ERROR_MESSAGE, result.getBody());
    }

    @Test
    void testHandleBankTransactionFailedException_returns_internal_server_error_with_exception_message() {
        var result = errorHandler.handleBankTransactionFailedException(
                new BankTransactionFailedException(ERROR_MESSAGE));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertEquals(ERROR_MESSAGE, result.getBody());
    }
}
