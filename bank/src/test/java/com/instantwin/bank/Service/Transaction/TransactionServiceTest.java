package com.instantwin.bank.service.Transaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.instantwin.bank.DTO.Transaction.TransactionDTO;
import com.instantwin.bank.DTO.Transaction.TransactionRequestUser;
import com.instantwin.bank.DTO.Transaction.TransactionUpdateDTO;
import com.instantwin.bank.contract.Client.Transaction.ITransactionUserClient;
import com.instantwin.bank.contract.Model.Transaction.ITransactionFactory;
import com.instantwin.bank.model.Transaction.TransactionEntity;
import com.instantwin.bank.repository.Transaction.ITransactionRepository;
import com.instantwin.bank.service.Transaction.TransactionService;
import com.instantwin.bank.utilities.Transaction.TransactionInvoicingParty;

public class TransactionServiceTest {

    private static final long USER_ID = 1L;
    private static final long OTHER_USER_ID = 2L;
    private static final long TRANSACTION_ID = 10L;
    private static final long UNKNOWN_ID = 999L;

    private static final BigDecimal AMOUNT = BigDecimal.valueOf(10);
    private static final BigDecimal OTHER_AMOUNT = BigDecimal.valueOf(-3);
    private static final BigDecimal UPDATED_AMOUNT = BigDecimal.valueOf(20);

    private ITransactionRepository transactionRepository;
    private ITransactionUserClient transactionUserClient;
    private ITransactionFactory transactionFactory;
    private TransactionService transactionService;

    @BeforeEach
    void setUpService() {
        transactionRepository = mock(ITransactionRepository.class);
        transactionUserClient = mock(ITransactionUserClient.class);
        transactionFactory = mock(ITransactionFactory.class);
        transactionService = new TransactionService(
                transactionRepository,
                transactionUserClient,
                transactionFactory);
    }

    @Test
    void testCreateTransaction_creates_transaction_from_correct_DTO() {
        var entity = transaction(
                TRANSACTION_ID,
                USER_ID,
                AMOUNT,
                TransactionInvoicingParty.USER_SLICE);
        var request = new TransactionDTO(AMOUNT, TransactionInvoicingParty.USER_SLICE);
        userExists(USER_ID);
        when(transactionFactory.createTransaction(
                USER_ID,
                AMOUNT,
                TransactionInvoicingParty.USER_SLICE)).thenReturn(entity);
        when(transactionRepository.save(entity)).thenReturn(entity);

        var result = transactionService.createTransaction(USER_ID, request);

        assertTrue(result.isPresent());
        assertEquals(TRANSACTION_ID, result.get().getId());
        assertEquals(USER_ID, result.get().getUserId());
        assertEquals(AMOUNT, result.get().getAmount());
        assertEquals(TransactionInvoicingParty.USER_SLICE, result.get().getInvoicingParty());
        verify(transactionFactory).createTransaction(
                USER_ID,
                AMOUNT,
                TransactionInvoicingParty.USER_SLICE);
    }

    @Test
    void testCreateTransaction_returns_empty_optional_when_user_does_not_exist() {
        var request = new TransactionDTO(AMOUNT, TransactionInvoicingParty.USER_SLICE);
        when(transactionUserClient.checkIfUserExists(UNKNOWN_ID)).thenReturn(Optional.empty());

        var result = transactionService.createTransaction(UNKNOWN_ID, request);

        assertTrue(result.isEmpty());
        verify(transactionFactory, never()).createTransaction(anyLong(), any(), any());
    }

    @Test
    void testCreateTransaction_saves_transaction_to_repository() {
        var entity = transaction(
                TRANSACTION_ID,
                USER_ID,
                AMOUNT,
                TransactionInvoicingParty.USER_SLICE);
        var request = new TransactionDTO(AMOUNT, TransactionInvoicingParty.USER_SLICE);
        userExists(USER_ID);
        when(transactionFactory.createTransaction(
                USER_ID,
                AMOUNT,
                TransactionInvoicingParty.USER_SLICE)).thenReturn(entity);
        when(transactionRepository.save(entity)).thenReturn(entity);

        transactionService.createTransaction(USER_ID, request);

        verify(transactionRepository).save(entity);
    }

    @Test
    void testDeleteTransaction_returns_empty_optional_when_transaction_does_not_exist() {
        when(transactionRepository.findById(UNKNOWN_ID)).thenReturn(Optional.empty());

        var result = transactionService.deleteTransaction(UNKNOWN_ID);

        assertTrue(result.isEmpty());
    }

    @Test
    void testDeleteTransaction_deletes_transaction_from_repository() {
        var entity = transaction(
                TRANSACTION_ID,
                USER_ID,
                AMOUNT,
                TransactionInvoicingParty.SLOTS);
        when(transactionRepository.findById(TRANSACTION_ID)).thenReturn(Optional.of(entity));

        var result = transactionService.deleteTransaction(TRANSACTION_ID);

        assertTrue(result.isPresent());
        verify(transactionRepository).delete(entity);
    }

    @Test
    void testDeleteTransaction_returns_transaction_delete_view_with_correct_values() {
        var entity = transaction(
                TRANSACTION_ID,
                USER_ID,
                AMOUNT,
                TransactionInvoicingParty.ROULETTE);
        when(transactionRepository.findById(TRANSACTION_ID)).thenReturn(Optional.of(entity));

        var result = transactionService.deleteTransaction(TRANSACTION_ID);

        assertTrue(result.isPresent());
        assertEquals(USER_ID, result.get().userId());
        assertEquals(AMOUNT, result.get().amount());
        assertEquals(TransactionInvoicingParty.ROULETTE, result.get().invoicingParty());
    }

    @Test
    void testFindAllTransactions_returns_all_transactions() {
        var first = transaction(
                TRANSACTION_ID,
                USER_ID,
                AMOUNT,
                TransactionInvoicingParty.USER_SLICE);
        var second = transaction(
                TRANSACTION_ID + 1,
                OTHER_USER_ID,
                OTHER_AMOUNT,
                TransactionInvoicingParty.SLOTS);
        when(transactionRepository.findAll()).thenReturn(List.of(first, second));

        var result = transactionService.findAllTransactions();

        assertEquals(2, result.size());
        assertEquals(TRANSACTION_ID, result.get(0).getId());
        assertEquals(USER_ID, result.get(0).getUserId());
        assertEquals(AMOUNT, result.get(0).getAmount());
        assertEquals(TRANSACTION_ID + 1, result.get(1).getId());
        assertEquals(OTHER_USER_ID, result.get(1).getUserId());
        assertEquals(OTHER_AMOUNT, result.get(1).getAmount());
    }

    @Test
    void testFindAllTransactions_returns_empty_list_when_no_transactions_exist() {
        when(transactionRepository.findAll()).thenReturn(List.of());

        var result = transactionService.findAllTransactions();

        assertTrue(result.isEmpty());
    }

    @Test
    void testFindTransactionById_returns_right_transaction() {
        var entity = transaction(
                TRANSACTION_ID,
                USER_ID,
                AMOUNT,
                TransactionInvoicingParty.USER_SLICE);
        when(transactionRepository.findById(TRANSACTION_ID)).thenReturn(Optional.of(entity));

        var result = transactionService.getTransactionById(TRANSACTION_ID);

        assertTrue(result.isPresent());
        assertEquals(TRANSACTION_ID, result.get().getId());
        assertEquals(USER_ID, result.get().getUserId());
        assertEquals(AMOUNT, result.get().getAmount());
        assertEquals(TransactionInvoicingParty.USER_SLICE, result.get().getInvoicingParty());
    }

    @Test
    void testFindTransactionById_returns_empty_optional_when_transaction_does_not_exist() {
        when(transactionRepository.findById(UNKNOWN_ID)).thenReturn(Optional.empty());

        var result = transactionService.getTransactionById(UNKNOWN_ID);

        assertTrue(result.isEmpty());
    }

    @Test
    void testFindTransactionsByUserId_returns_all_transactions_of_user() {
        var first = transaction(
                TRANSACTION_ID,
                USER_ID,
                AMOUNT,
                TransactionInvoicingParty.USER_SLICE);
        var second = transaction(
                TRANSACTION_ID + 1,
                USER_ID,
                OTHER_AMOUNT,
                TransactionInvoicingParty.SLOTS);
        userExists(USER_ID);
        when(transactionRepository.findAllByUserId(USER_ID)).thenReturn(List.of(first, second));

        var result = transactionService.findTransactionsByUserId(USER_ID);

        assertTrue(result.isPresent());
        assertEquals(2, result.get().size());
        assertEquals(USER_ID, result.get().get(0).getUserId());
        assertEquals(AMOUNT, result.get().get(0).getAmount());
        assertEquals(USER_ID, result.get().get(1).getUserId());
        assertEquals(OTHER_AMOUNT, result.get().get(1).getAmount());
    }

    @Test
    void testFindTransactionsByUserId_returns_empty_list_when_user_has_no_transactions() {
        userExists(USER_ID);
        when(transactionRepository.findAllByUserId(USER_ID)).thenReturn(List.of());

        var result = transactionService.findTransactionsByUserId(USER_ID);

        assertTrue(result.isPresent());
        assertTrue(result.get().isEmpty());
    }

    @Test
    void testFindTransactionsByUserId_returns_empty_list_when_user_does_not_exist() {
        when(transactionUserClient.checkIfUserExists(UNKNOWN_ID)).thenReturn(Optional.empty());

        var result = transactionService.findTransactionsByUserId(UNKNOWN_ID);

        assertTrue(result.isEmpty());
        verify(transactionRepository, never()).findAllByUserId(UNKNOWN_ID);
    }

    @Test
    void testUpdateTransaction_returns_empty_if_transaction_does_not_exist() {
        var request = updateRequest(USER_ID);
        when(transactionRepository.findById(UNKNOWN_ID)).thenReturn(Optional.empty());

        var result = transactionService.updateTransaction(UNKNOWN_ID, request);

        assertTrue(result.isEmpty());
    }

    @Test
    void testUpdateTransaction_returns_empty_if_user_does_not_exist() {
        var entity = transaction(
                TRANSACTION_ID,
                USER_ID,
                AMOUNT,
                TransactionInvoicingParty.USER_SLICE);
        var request = updateRequest(UNKNOWN_ID);
        when(transactionRepository.findById(TRANSACTION_ID)).thenReturn(Optional.of(entity));
        when(transactionUserClient.checkIfUserExists(UNKNOWN_ID)).thenReturn(Optional.empty());

        var result = transactionService.updateTransaction(TRANSACTION_ID, request);

        assertTrue(result.isEmpty());
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void testUpdateTransaction_updates_transaction_with_correct_values() {
        var entity = transaction(
                TRANSACTION_ID,
                USER_ID,
                AMOUNT,
                TransactionInvoicingParty.USER_SLICE);
        var request = updateRequest(OTHER_USER_ID);
        when(transactionRepository.findById(TRANSACTION_ID)).thenReturn(Optional.of(entity));
        userExists(OTHER_USER_ID);
        when(transactionRepository.save(entity)).thenReturn(entity);

        var result = transactionService.updateTransaction(TRANSACTION_ID, request);

        assertTrue(result.isPresent());
        verify(entity).updateUserId(OTHER_USER_ID);
        verify(entity).updateAmount(UPDATED_AMOUNT);
        verify(entity).updateInvoicingParty(TransactionInvoicingParty.ROULETTE);
        verify(transactionRepository).save(entity);
    }

    @Test
    void testUpdateTransaction_returns_updated_transaction() {
        var entity = transaction(
                TRANSACTION_ID,
                USER_ID,
                AMOUNT,
                TransactionInvoicingParty.USER_SLICE);
        var request = updateRequest(OTHER_USER_ID);
        when(transactionRepository.findById(TRANSACTION_ID)).thenReturn(Optional.of(entity));
        userExists(OTHER_USER_ID);
        when(transactionRepository.save(entity)).thenReturn(entity);

        var result = transactionService.updateTransaction(TRANSACTION_ID, request);

        assertTrue(result.isPresent());
        assertEquals(TRANSACTION_ID, result.get().getId());
        assertEquals(OTHER_USER_ID, result.get().getUserId());
        assertEquals(UPDATED_AMOUNT, result.get().getAmount());
        assertEquals(TransactionInvoicingParty.ROULETTE, result.get().getInvoicingParty());
    }

    private void userExists(long userId) {
        when(transactionUserClient.checkIfUserExists(userId))
                .thenReturn(Optional.of(new TransactionRequestUser(userId)));
    }

    private TransactionUpdateDTO updateRequest(long userId) {
        return new TransactionUpdateDTO(
                userId,
                UPDATED_AMOUNT,
                TransactionInvoicingParty.ROULETTE);
    }

    private TransactionEntity transaction(
            long id,
            long userId,
            BigDecimal amount,
            TransactionInvoicingParty invoicingParty) {
        var entity = mock(TransactionEntity.class);
        var currentUserId = new AtomicReference<>(userId);
        var currentAmount = new AtomicReference<>(amount);
        var currentInvoicingParty = new AtomicReference<>(invoicingParty);

        when(entity.getId()).thenReturn(id);
        when(entity.getUserId()).thenAnswer(ignored -> currentUserId.get());
        when(entity.getAmount()).thenAnswer(ignored -> currentAmount.get());
        when(entity.getInvoicingParty()).thenAnswer(ignored -> currentInvoicingParty.get());

        doAnswer(invocation -> {
            currentUserId.set(invocation.getArgument(0));
            return null;
        }).when(entity).updateUserId(anyLong());
        doAnswer(invocation -> {
            currentAmount.set(invocation.getArgument(0));
            return null;
        }).when(entity).updateAmount(any(BigDecimal.class));
        doAnswer(invocation -> {
            currentInvoicingParty.set(invocation.getArgument(0));
            return null;
        }).when(entity).updateInvoicingParty(any(TransactionInvoicingParty.class));

        return entity;
    }
}
