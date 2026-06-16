package com.instantwin.bank.Model.Transaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.instantwin.bank.Utilities.Transaction.TransactionErrorMessages;
import com.instantwin.bank.Utilities.Transaction.TransactionInvoicingParty;
import com.instantwin.bank.Utilities.Transaction.UserIdInputFormatInvalidException;

public class TransactionEntityTest {

        private static final long VALID_USER_ID = 1L;
        private static final long ZERO_USER_ID = 0L;
        private static final long NEGATIVE_USER_ID = -1L;

        private static final BigDecimal VALID_AMOUNT = BigDecimal.TEN;
        private static final BigDecimal UPDATED_AMOUNT = BigDecimal.valueOf(20);

        private static final TransactionInvoicingParty VALID_INVOICING_PARTY = TransactionInvoicingParty.USER_SLICE;

        private static final TransactionInvoicingParty UPDATED_INVOICING_PARTY = TransactionInvoicingParty.ROULETTE;

        private TransactionEntity transaction;

        @BeforeEach
        void setUp() {
                transaction = TransactionEntity.of(
                                VALID_USER_ID,
                                VALID_INVOICING_PARTY,
                                VALID_AMOUNT);
        }

        @Test
        void testOf_creates_transaction_entity_with_valid_input() {
                assertEquals(VALID_USER_ID, transaction.getUserId());
                assertEquals(VALID_AMOUNT, transaction.getAmount());
                assertEquals(VALID_INVOICING_PARTY, transaction.getInvoicingParty());
        }

        @Test
        void testUpdateUserId_updates_user_id_when_input_is_valid() {
                transaction.updateUserId(VALID_USER_ID);

                assertEquals(VALID_USER_ID, transaction.getUserId());
        }

        @Test
        void testUpdateUserId_throws_exception_when_user_id_is_zero() {
                var exception = assertThrows(
                                UserIdInputFormatInvalidException.class,
                                () -> transaction.updateUserId(ZERO_USER_ID));

                assertEquals(
                                TransactionErrorMessages.USER_ID_INPUT_NEGATIVE,
                                exception.getMessage());
        }

        @Test
        void testUpdateUserId_throws_exception_when_user_id_is_negative() {
                var exception = assertThrows(
                                UserIdInputFormatInvalidException.class,
                                () -> transaction.updateUserId(NEGATIVE_USER_ID));

                assertEquals(
                                TransactionErrorMessages.USER_ID_INPUT_NEGATIVE,
                                exception.getMessage());
        }

        @Test
        void testUpdateAmount_updates_amount_when_input_is_valid() {
                transaction.updateAmount(UPDATED_AMOUNT);

                assertEquals(UPDATED_AMOUNT, transaction.getAmount());
        }

        @Test
        void testUpdateAmount_throws_exception_when_amount_is_null() {
                var exception = assertThrows(
                                IllegalArgumentException.class,
                                () -> transaction.updateAmount(null));

                assertEquals(
                                TransactionErrorMessages.AMOUNT_INPUT_NULL,
                                exception.getMessage());
        }

        @Test
        void testUpdateInvoicingParty_updates_invoicing_party_when_input_is_valid() {
                transaction.updateInvoicingParty(UPDATED_INVOICING_PARTY);

                assertEquals(UPDATED_INVOICING_PARTY, transaction.getInvoicingParty());
        }

        @Test
        void testUpdateInvoicingParty_throws_exception_when_invoicing_party_is_null() {
                var exception = assertThrows(
                                IllegalArgumentException.class,
                                () -> transaction.updateInvoicingParty(null));

                assertEquals(
                                TransactionErrorMessages.INVOICING_PARTY_INPUT_NULL,
                                exception.getMessage());
        }
}
