package com.instantwin.bank.utilities.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class UserExceptionHandlerTest {

    private static final String ERROR_MESSAGE = "Test error";

    private UserExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new UserExceptionHandler();
    }

    @Test
    void testHandleModelValidityBreachException_returns_bad_request_with_exception_message() {
        var result = exceptionHandler.handleModelValidityBreachException(
                new ModelValidityBreachException(ERROR_MESSAGE));

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals(ERROR_MESSAGE, result.getBody());
    }

    @Test
    void testHandleDecimalPlaceInvalidException_returns_bad_request_with_exception_message() {
        var result = exceptionHandler.handleDecimalPlaceInvalidException(
                new DecimalPlaceInvalidException(ERROR_MESSAGE));

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals(ERROR_MESSAGE, result.getBody());
    }

    @Test
    void testHandleAmountNegativeException_returns_bad_request_with_exception_message() {
        var result = exceptionHandler.handleAmountNegativeException(
                new AmountNegativeException(ERROR_MESSAGE));

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals(ERROR_MESSAGE, result.getBody());
    }

    @Test
    void testHandleTransactionRequestFailedException_returns_internal_server_error_with_exception_message() {
        var result = exceptionHandler.handleTransactionRequestFailedException(
                new TransactionRequestFailedException(ERROR_MESSAGE));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertEquals(ERROR_MESSAGE, result.getBody());
    }
}
