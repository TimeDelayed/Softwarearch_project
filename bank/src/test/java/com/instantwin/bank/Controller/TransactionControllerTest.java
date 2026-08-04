package com.instantwin.bank.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import com.instantwin.bank.DTO.Transaction.TransactionDTO;
import com.instantwin.bank.contract.Service.Transaction.ITransactionService;
import com.instantwin.bank.controller.Transaction.TransactionController;
import com.instantwin.bank.utilities.Transaction.TransactionInvoicingParty;
import com.instantwin.bank.view.Transaction.TransactionView;

public class TransactionControllerTest {

    private static final long USER_ID = 1L;
    private static final BigDecimal AMOUNT = new BigDecimal("10.00");

    private ITransactionService transactionService;
    private TransactionController transactionController;

    @BeforeEach
    void setUp() {
        transactionService = mock(ITransactionService.class);
        transactionController = new TransactionController(transactionService);
    }

    @Test
    void testCreateTransaction_returns_created_transaction_with_201_status() {
        var request = new TransactionDTO(AMOUNT, TransactionInvoicingParty.ROULETTE);
        var view = new TransactionView(10L, USER_ID, AMOUNT, TransactionInvoicingParty.ROULETTE);
        when(transactionService.createTransaction(USER_ID, request)).thenReturn(Optional.of(view));

        var result = transactionController.createTransaction(USER_ID, request);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(view, result.getBody());
        verify(transactionService).createTransaction(USER_ID, request);
    }

    @Test
    void testCreateTransaction_returns_not_found_when_user_does_not_exist() {
        var request = new TransactionDTO(AMOUNT, TransactionInvoicingParty.ROULETTE);
        when(transactionService.createTransaction(USER_ID, request)).thenReturn(Optional.empty());

        var result = transactionController.createTransaction(USER_ID, request);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertNull(result.getBody());
    }
}
