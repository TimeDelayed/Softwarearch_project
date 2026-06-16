package com.instantwin.bank.Service.Transaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.instantwin.bank.DTO.Transaction.TransactionDTO;
import com.instantwin.bank.DTO.Transaction.TransactionRequestUser;
import com.instantwin.bank.DTO.Transaction.TransactionUpdateDTO;
import com.instantwin.bank.Model.Transaction.TransactionEntity;
import com.instantwin.bank.Repository.Transaction.ITransactionRepository;
import com.instantwin.bank.Utilities.Transaction.TransactionInvoicingParty;
import com.instantwin.bank.contract.Client.Transaction.ITransactionUserClient;
import com.instantwin.bank.contract.Model.Transaction.ITransactionFactory;

public class TransactionServiceTest {

    private static final long USER_1_ID = 1L;
    private static final long USER_2_ID = 2L;
    private static final long USER_WITH_NO_TRANSACTIONS_ID = 3L;

    private static final long TRANSACTION_1_ID = 1L;
    private static final long TRANSACTION_2_ID = 2L;
    private static final long TRANSACTION_3_ID = 3L;
    private static final long TRANSACTION_4_ID = 4L;
    private static final long NON_EXISTENT_TRANSACTION_ID = 999L;

    private static final BigDecimal TRANSACTION_1_AMOUNT = BigDecimal.valueOf(10);
    private static final BigDecimal TRANSACTION_2_AMOUNT = BigDecimal.valueOf(-3);
    private static final BigDecimal TRANSACTION_3_AMOUNT = BigDecimal.valueOf(5);
    private static final BigDecimal TRANSACTION_4_AMOUNT = BigDecimal.valueOf(-2);
    private static final BigDecimal UPDATED_TRANSACTION_AMOUNT = BigDecimal.valueOf(20);

    private TransactionService transactionService;
    private ITransactionRepository transactionRepository;
    private ITransactionUserClient transactionUserClient;
    private ITransactionFactory transactionFactory;

    private TransactionEntity transaction1;
    private TransactionEntity transaction2;
    private TransactionEntity transaction3;
    private TransactionEntity transaction4;

    private List<TransactionEntity> allTransactions;
    private List<TransactionEntity> transactionsByUserId1;
    private List<TransactionEntity> transactionsByUserId2;

    @BeforeEach
    void setUp() {
        this.transactionRepository = mock(ITransactionRepository.class);
        this.transactionUserClient = mock(ITransactionUserClient.class);
        this.transactionFactory = mock(ITransactionFactory.class);
        this.transactionService = new TransactionService(
                transactionRepository,
                transactionUserClient,
                transactionFactory);

        this.transaction1 = mock(TransactionEntity.class);
        when(transaction1.getId()).thenReturn(TRANSACTION_1_ID);
        when(transaction1.getUserId()).thenReturn(USER_1_ID);
        when(transaction1.getAmount()).thenReturn(TRANSACTION_1_AMOUNT);
        when(transaction1.getInvoicingParty()).thenReturn(TransactionInvoicingParty.USER_SLICE);
        doAnswer(invocation -> {
            long userId = invocation.getArgument(0);
            when(transaction1.getUserId()).thenReturn(userId);
            return null;
        }).when(transaction1).updateUserId(anyLong());
        doAnswer(invocation -> {
            BigDecimal amount = invocation.getArgument(0);
            when(transaction1.getAmount()).thenReturn(amount);
            return null;
        }).when(transaction1).updateAmount(any(BigDecimal.class));
        doAnswer(invocation -> {
            TransactionInvoicingParty invoicingParty = invocation.getArgument(0);
            when(transaction1.getInvoicingParty()).thenReturn(invoicingParty);
            return null;
        }).when(transaction1).updateInvoicingParty(any(TransactionInvoicingParty.class));

        this.transaction2 = mock(TransactionEntity.class);
        when(transaction2.getId()).thenReturn(TRANSACTION_2_ID);
        when(transaction2.getUserId()).thenReturn(USER_1_ID);
        when(transaction2.getAmount()).thenReturn(TRANSACTION_2_AMOUNT);
        when(transaction2.getInvoicingParty()).thenReturn(TransactionInvoicingParty.SLOTS);
        doAnswer(invocation -> {
            long userId = invocation.getArgument(0);
            when(transaction2.getUserId()).thenReturn(userId);
            return null;
        }).when(transaction2).updateUserId(anyLong());
        doAnswer(invocation -> {
            BigDecimal amount = invocation.getArgument(0);
            when(transaction2.getAmount()).thenReturn(amount);
            return null;
        }).when(transaction2).updateAmount(any(BigDecimal.class));
        doAnswer(invocation -> {
            TransactionInvoicingParty invoicingParty = invocation.getArgument(0);
            when(transaction2.getInvoicingParty()).thenReturn(invoicingParty);
            return null;
        }).when(transaction2).updateInvoicingParty(any(TransactionInvoicingParty.class));

        this.transaction3 = mock(TransactionEntity.class);
        when(transaction3.getId()).thenReturn(TRANSACTION_3_ID);
        when(transaction3.getUserId()).thenReturn(USER_2_ID);
        when(transaction3.getAmount()).thenReturn(TRANSACTION_3_AMOUNT);
        when(transaction3.getInvoicingParty()).thenReturn(TransactionInvoicingParty.USER_SLICE);
        doAnswer(invocation -> {
            long userId = invocation.getArgument(0);
            when(transaction3.getUserId()).thenReturn(userId);
            return null;
        }).when(transaction3).updateUserId(anyLong());
        doAnswer(invocation -> {
            BigDecimal amount = invocation.getArgument(0);
            when(transaction3.getAmount()).thenReturn(amount);
            return null;
        }).when(transaction3).updateAmount(any(BigDecimal.class));
        doAnswer(invocation -> {
            TransactionInvoicingParty invoicingParty = invocation.getArgument(0);
            when(transaction3.getInvoicingParty()).thenReturn(invoicingParty);
            return null;
        }).when(transaction3).updateInvoicingParty(any(TransactionInvoicingParty.class));

        this.transaction4 = mock(TransactionEntity.class);
        when(transaction4.getId()).thenReturn(TRANSACTION_4_ID);
        when(transaction4.getUserId()).thenReturn(USER_2_ID);
        when(transaction4.getAmount()).thenReturn(TRANSACTION_4_AMOUNT);
        when(transaction4.getInvoicingParty()).thenReturn(TransactionInvoicingParty.ROULETTE);
        doAnswer(invocation -> {
            long userId = invocation.getArgument(0);
            when(transaction4.getUserId()).thenReturn(userId);
            return null;
        }).when(transaction4).updateUserId(anyLong());
        doAnswer(invocation -> {
            BigDecimal amount = invocation.getArgument(0);
            when(transaction4.getAmount()).thenReturn(amount);
            return null;
        }).when(transaction4).updateAmount(any(BigDecimal.class));
        doAnswer(invocation -> {
            TransactionInvoicingParty invoicingParty = invocation.getArgument(0);
            when(transaction4.getInvoicingParty()).thenReturn(invoicingParty);
            return null;
        }).when(transaction4).updateInvoicingParty(any(TransactionInvoicingParty.class));

        this.allTransactions = List.of(transaction1, transaction2, transaction3, transaction4);
        this.transactionsByUserId1 = List.of(transaction1, transaction2);
        this.transactionsByUserId2 = List.of(transaction3, transaction4);

        when(transactionRepository.findAll()).thenReturn(this.allTransactions);
        when(transactionRepository.findById(anyLong())).thenAnswer(invocation -> {
            var id = invocation.getArgument(0);

            if (Objects.equals(id, TRANSACTION_1_ID)) {
                return Optional.of(transaction1);
            }

            if (Objects.equals(id, TRANSACTION_2_ID)) {
                return Optional.of(transaction2);
            }

            if (Objects.equals(id, TRANSACTION_3_ID)) {
                return Optional.of(transaction3);
            }

            if (Objects.equals(id, TRANSACTION_4_ID)) {
                return Optional.of(transaction4);
            }

            return Optional.empty();
        });

        when(transactionRepository.findAllByUserId(anyLong())).thenAnswer(invocation -> {
            var userId = invocation.getArgument(0);

            if (Objects.equals(userId, USER_1_ID)) {
                return this.transactionsByUserId1;
            }

            if (Objects.equals(userId, USER_2_ID)) {
                return this.transactionsByUserId2;
            }

            if (Objects.equals(userId, USER_WITH_NO_TRANSACTIONS_ID)) {
                return List.of();
            }

            return List.of();
        });

        when(transactionRepository.save(any(TransactionEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(transactionFactory.createTransaction(
                this.transaction1.getUserId(),
                this.transaction1.getAmount(),
                this.transaction1.getInvoicingParty()))
                .thenReturn(this.transaction1);

        when(transactionFactory.createTransaction(
                this.transaction2.getUserId(),
                this.transaction2.getAmount(),
                this.transaction2.getInvoicingParty()))
                .thenReturn(this.transaction2);

        when(transactionFactory.createTransaction(
                this.transaction3.getUserId(),
                this.transaction3.getAmount(),
                this.transaction3.getInvoicingParty()))
                .thenReturn(this.transaction3);

        when(transactionFactory.createTransaction(
                this.transaction4.getUserId(),
                this.transaction4.getAmount(),
                this.transaction4.getInvoicingParty()))
                .thenReturn(this.transaction4);

        TransactionRequestUser user1 = new TransactionRequestUser(USER_1_ID);
        TransactionRequestUser user2 = new TransactionRequestUser(USER_2_ID);
        TransactionRequestUser userWithNoTransactions = new TransactionRequestUser(USER_WITH_NO_TRANSACTIONS_ID);

        when(transactionUserClient.checkIfUserExists(anyLong()))
                .thenAnswer(invocation -> {
                    var id = invocation.getArgument(0);

                    if (Objects.equals(id, USER_1_ID)) {
                        return Optional.of(user1);
                    }

                    if (Objects.equals(id, USER_2_ID)) {
                        return Optional.of(user2);
                    }

                    if (Objects.equals(id, USER_WITH_NO_TRANSACTIONS_ID)) {
                        return Optional.of(userWithNoTransactions);
                    }

                    return Optional.empty();
                });
    }

    @Test
    void testCreateTransaction_creates_transaction_from_correct_DTO() {
        TransactionDTO transactionRequest = new TransactionDTO(
                this.transaction1.getAmount(),
                this.transaction1.getInvoicingParty());

        var result = transactionService.createTransaction(USER_1_ID, transactionRequest);

        verify(transactionFactory).createTransaction(
                USER_1_ID,
                TRANSACTION_1_AMOUNT,
                TransactionInvoicingParty.USER_SLICE);

        assertTrue(result.isPresent());
        assertEquals(this.transaction1.getId(), result.get().getId());
        assertEquals(this.transaction1.getUserId(), result.get().getUserId());
        assertEquals(this.transaction1.getAmount(), result.get().getAmount());
        assertEquals(this.transaction1.getInvoicingParty(), result.get().getInvoicingParty());
    }

    @Test
    void testCreateTransaction_returns_empty_optional_when_user_does_not_exist() {
        TransactionDTO transactionRequest = new TransactionDTO(
                TRANSACTION_1_AMOUNT,
                TransactionInvoicingParty.USER_SLICE);

        var result = transactionService.createTransaction(NON_EXISTENT_TRANSACTION_ID, transactionRequest);

        assertTrue(result.isEmpty());
    }

    @Test
    void testCreateTransaction_saves_transaction_to_repository() {
        TransactionDTO transactionRequest = new TransactionDTO(
                this.transaction1.getAmount(),
                this.transaction1.getInvoicingParty());

        var result = transactionService.createTransaction(USER_1_ID, transactionRequest);

        assertTrue(result.isPresent());
        verify(transactionRepository).save(this.transaction1);
    }

    @Test
    void testDeleteTransaction_returns_empty_optional_when_transaction_does_not_exist() {
        var result = transactionService.deleteTransaction(NON_EXISTENT_TRANSACTION_ID);

        assertTrue(result.isEmpty());
    }

    @Test
    void testDeleteTransaction_deletes_transaction_from_repository() {
        var result = transactionService.deleteTransaction(this.transaction2.getId());

        assertTrue(result.isPresent());
        verify(transactionRepository).delete(this.transaction2);
    }

    @Test
    void testDeleteTransaction_returns_transaction_delete_view_with_correct_values() {
        var result = transactionService.deleteTransaction(this.transaction4.getId());

        assertTrue(result.isPresent());
        assertEquals(this.transaction4.getUserId(), result.get().userId());
        assertEquals(this.transaction4.getAmount(), result.get().amount());
        assertEquals(this.transaction4.getInvoicingParty(), result.get().invoicingParty());
    }

    @Test
    void testFindAllTransactions_returns_all_transactions() {
        var result = transactionService.findAllTransactions();

        assertEquals(this.allTransactions.size(), result.size());

        for (int i = 0; i < result.size(); i++) {
            assertEquals(this.allTransactions.get(i).getId(), result.get(i).getId());
            assertEquals(this.allTransactions.get(i).getUserId(), result.get(i).getUserId());
            assertEquals(this.allTransactions.get(i).getAmount(), result.get(i).getAmount());
            assertEquals(this.allTransactions.get(i).getInvoicingParty(), result.get(i).getInvoicingParty());
        }
    }

    @Test
    void testFindAllTransactions_returns_empty_list_when_no_transactions_exist() {
        when(transactionRepository.findAll()).thenReturn(List.of());

        var result = transactionService.findAllTransactions();

        assertTrue(result.isEmpty());
    }

    @Test
    void testFindTransactionById_returns_right_transaction() {
        var result = transactionService.getTransactionById(this.transaction3.getId());

        assertTrue(result.isPresent());
        assertEquals(this.transaction3.getId(), result.get().getId());
        assertEquals(this.transaction3.getUserId(), result.get().getUserId());
        assertEquals(this.transaction3.getAmount(), result.get().getAmount());
        assertEquals(this.transaction3.getInvoicingParty(), result.get().getInvoicingParty());
    }

    @Test
    void testFindTransactionById_returns_empty_optional_when_transaction_does_not_exist() {
        var result = transactionService.getTransactionById(NON_EXISTENT_TRANSACTION_ID);

        assertTrue(result.isEmpty());
    }

    @Test
    void testFindTransactionsByUserId_returns_all_transactions_of_user() {
        var result = transactionService.findTransactionsByUserId(USER_1_ID);

        assertTrue(result.isPresent());
        assertEquals(this.transactionsByUserId1.size(), result.get().size());

        for (int i = 0; i < result.get().size(); i++) {
            assertEquals(
                    this.transactionsByUserId1.get(i).getUserId(),
                    result.get().get(i).getUserId());

            assertEquals(
                    this.transactionsByUserId1.get(i).getAmount(),
                    result.get().get(i).getAmount());
        }
    }

    @Test
    void testFindTransactionsByUserId_returns_empty_list_when_user_has_no_transactions() {
        var result = transactionService.findTransactionsByUserId(USER_WITH_NO_TRANSACTIONS_ID);

        assertTrue(result.isPresent());
        
        var transactions = result.get();
        assertTrue(transactions.isEmpty());
    }

    @Test
    void testFindTransactionsByUserId_returns_empty_list_when_user_does_not_exist() {
        var result = transactionService.findTransactionsByUserId(NON_EXISTENT_TRANSACTION_ID);

        assertFalse(result.isPresent());
        assertTrue(result.isEmpty());
    }

    @Test
    void testUpdateTransaction_returns_empty_if_transaction_does_not_exist() {
        TransactionUpdateDTO transactionRequest = new TransactionUpdateDTO(
                USER_1_ID,
                UPDATED_TRANSACTION_AMOUNT,
                TransactionInvoicingParty.ROULETTE);

        var result = transactionService.updateTransaction(NON_EXISTENT_TRANSACTION_ID, transactionRequest);

        assertTrue(result.isEmpty());
    }

    @Test
    void testUpdateTransaction_returns_empty_if_user_does_not_exist() {
        TransactionUpdateDTO transactionRequest = new TransactionUpdateDTO(
                NON_EXISTENT_TRANSACTION_ID,
                UPDATED_TRANSACTION_AMOUNT,
                TransactionInvoicingParty.ROULETTE);

        var result = transactionService.updateTransaction(this.transaction1.getId(), transactionRequest);

        assertTrue(result.isEmpty());
    }

    @Test
    void testUpdateTransaction_updates_transaction_with_correct_values() {
        TransactionUpdateDTO transactionRequest = new TransactionUpdateDTO(
                USER_1_ID,
                UPDATED_TRANSACTION_AMOUNT,
                TransactionInvoicingParty.ROULETTE);

        var result = transactionService.updateTransaction(this.transaction1.getId(), transactionRequest);

        assertTrue(result.isPresent());

        verify(this.transaction1).updateUserId(USER_1_ID);
        verify(this.transaction1).updateAmount(UPDATED_TRANSACTION_AMOUNT);
        verify(this.transaction1).updateInvoicingParty(TransactionInvoicingParty.ROULETTE);

        verify(transactionRepository).save(this.transaction1);
    }

    @Test
    void testUpdateTransaction_returns_updated_transaction() {
        TransactionUpdateDTO transactionRequest = new TransactionUpdateDTO(
                USER_1_ID,
                UPDATED_TRANSACTION_AMOUNT,
                TransactionInvoicingParty.ROULETTE);

        var result = transactionService.updateTransaction(this.transaction1.getId(), transactionRequest);

        assertTrue(result.isPresent());
        assertEquals(this.transaction1.getId(), result.get().getId());
        assertEquals(USER_1_ID, result.get().getUserId());
        assertEquals(UPDATED_TRANSACTION_AMOUNT, result.get().getAmount());
        assertEquals(TransactionInvoicingParty.ROULETTE, result.get().getInvoicingParty());
    }
}