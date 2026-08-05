package com.instantwin.slotmachine.utilities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class SlotErrorHandlerTest {

    private static final String ERROR_MESSAGE = "Test error";

    private SlotErrorHandler errorHandler;

    @BeforeEach
    void setUp() {
        errorHandler = new SlotErrorHandler();
    }

    @Test
    void testHandleGameRulesUnavailable_returns_internal_server_error_with_exception_message() {
        var result = errorHandler.handleGameRulesUnavailable(
                new GameRulesUnavailableException(ERROR_MESSAGE, new RuntimeException()));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertEquals(ERROR_MESSAGE, result.getBody());
    }

    @Test
    void testHandleBetAmountInvalid_returns_bad_request_with_exception_message() {
        var result = errorHandler.handleBetAmountInvalid(new BetAmountInvalidException(ERROR_MESSAGE));

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals(ERROR_MESSAGE, result.getBody());
    }

    @Test
    void testHandleInvalidSlotProbabilities_returns_internal_server_error_with_exception_message() {
        var result = errorHandler.handleInvalidSlotProbabilities(
                new InvalidSlotProbabilities(ERROR_MESSAGE));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertEquals(ERROR_MESSAGE, result.getBody());
    }

    @Test
    void testHandleModelValidityBreached_returns_bad_request_with_exception_message() {
        var result = errorHandler.handleModelValidityBreached(
                new ModelValidityBreachException(ERROR_MESSAGE));

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals(ERROR_MESSAGE, result.getBody());
    }
}
