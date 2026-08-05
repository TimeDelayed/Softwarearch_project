package com.instantwin.bank.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import com.instantwin.bank.DTO.Transaction.TransactionDTO;
import com.instantwin.bank.DTO.Transaction.TransactionUpdateDTO;
import com.instantwin.bank.contract.Service.Transaction.ITransactionService;
import com.instantwin.bank.controller.Transaction.TransactionController;
import com.instantwin.bank.utilities.Transaction.TransactionInvoicingParty;
import com.instantwin.bank.view.Transaction.TransactionDeleteView;
import com.instantwin.bank.view.Transaction.TransactionSpecificUserView;
import com.instantwin.bank.view.Transaction.TransactionView;

public class TransactionControllerTest {

    private static final long USER_ID = 1L;
    private static final long TRANSACTION_ID = 10L;
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

    @Test
    void testFindAllTransactions_returns_all_transactions_from_service() {
        var view = transactionView();
        when(transactionService.findAllTransactions()).thenReturn(List.of(view));

        var result = transactionController.findAllTransactions();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(List.of(view), result.getBody());
        verify(transactionService).findAllTransactions();
    }

    @Test
    void testFindTransactionsByUserId_returns_transactions_when_user_exists() {
        var view = new TransactionSpecificUserView(TRANSACTION_ID, AMOUNT);
        when(transactionService.findTransactionsByUserId(USER_ID))
                .thenReturn(Optional.of(List.of(view)));

        var result = transactionController.findTransactionsByUserId(USER_ID);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(List.of(view), result.getBody());
        verify(transactionService).findTransactionsByUserId(USER_ID);
    }

    @Test
    void testFindTransactionsByUserId_returns_empty_list_when_existing_user_has_no_transactions() {
        when(transactionService.findTransactionsByUserId(USER_ID))
                .thenReturn(Optional.of(List.of()));

        var result = transactionController.findTransactionsByUserId(USER_ID);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(List.of(), result.getBody());
    }

    @Test
    void testFindTransactionsByUserId_returns_not_found_when_user_does_not_exist() {
        when(transactionService.findTransactionsByUserId(USER_ID)).thenReturn(Optional.empty());

        var result = transactionController.findTransactionsByUserId(USER_ID);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    void testUpdateTransaction_returns_updated_transaction_when_transaction_and_user_exist() {
        var request = updateRequest();
        var view = transactionView();
        when(transactionService.updateTransaction(TRANSACTION_ID, request)).thenReturn(Optional.of(view));

        var result = transactionController.updateTransaction(TRANSACTION_ID, request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(view, result.getBody());
        verify(transactionService).updateTransaction(TRANSACTION_ID, request);
    }

    @Test
    void testUpdateTransaction_returns_not_found_when_update_is_rejected() {
        var request = updateRequest();
        when(transactionService.updateTransaction(TRANSACTION_ID, request)).thenReturn(Optional.empty());

        var result = transactionController.updateTransaction(TRANSACTION_ID, request);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    void testDeleteTransaction_returns_deleted_transaction_when_transaction_exists() {
        var view = new TransactionDeleteView(USER_ID, AMOUNT, TransactionInvoicingParty.ROULETTE);
        when(transactionService.deleteTransaction(TRANSACTION_ID)).thenReturn(Optional.of(view));

        var result = transactionController.deleteTransaction(TRANSACTION_ID);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(view, result.getBody());
        verify(transactionService).deleteTransaction(TRANSACTION_ID);
    }

    @Test
    void testDeleteTransaction_returns_not_found_when_transaction_does_not_exist() {
        when(transactionService.deleteTransaction(TRANSACTION_ID)).thenReturn(Optional.empty());

        var result = transactionController.deleteTransaction(TRANSACTION_ID);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertNull(result.getBody());
    }

    private TransactionView transactionView() {
        return new TransactionView(
                TRANSACTION_ID,
                USER_ID,
                AMOUNT,
                TransactionInvoicingParty.ROULETTE);
    }

    private TransactionUpdateDTO updateRequest() {
        return new TransactionUpdateDTO(
                USER_ID,
                AMOUNT,
                TransactionInvoicingParty.ROULETTE);
    }
}
