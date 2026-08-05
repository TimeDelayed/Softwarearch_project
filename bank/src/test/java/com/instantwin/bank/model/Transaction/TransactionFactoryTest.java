package com.instantwin.bank.model.Transaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import com.instantwin.bank.contract.Model.Transaction.ITransactionFactory;
import com.instantwin.bank.utilities.Transaction.TransactionInvoicingParty;

public class TransactionFactoryTest {

    @Test
    void testCreateTransaction_should_call_TransactionEntity_of() {
        long userId = 1L;
        BigDecimal amount = new BigDecimal("10.00");
        TransactionInvoicingParty invoicingParty = TransactionInvoicingParty.ROULETTE;
        ITransactionFactory factory = new TransactionFactory();

        try (MockedStatic<TransactionEntity> mocked = mockStatic(TransactionEntity.class)) {
            TransactionEntity transaction = mock(TransactionEntity.class);
            mocked.when(() -> TransactionEntity.of(userId, invoicingParty, amount))
                    .thenReturn(transaction);

            var result = factory.createTransaction(userId, amount, invoicingParty);

            assertEquals(transaction, result);
            mocked.verify(() -> TransactionEntity.of(userId, invoicingParty, amount));
        }
    }
}
