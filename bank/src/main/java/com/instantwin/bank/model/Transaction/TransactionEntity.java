package com.instantwin.bank.model.Transaction;

import java.math.BigDecimal;

import com.instantwin.bank.contract.Model.Transaction.ITransactionEntity;
import com.instantwin.bank.utilities.Transaction.TransactionErrorMessages;
import com.instantwin.bank.utilities.Transaction.TransactionInvoicingParty;
import com.instantwin.bank.utilities.Transaction.UserIdInputFormatInvalidException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "transactions")
@Getter
public class TransactionEntity implements ITransactionEntity{
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "userId", nullable = false)
    private Long userId;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "invoicingParty", nullable = false)
    private TransactionInvoicingParty invoicingParty;
    
    protected TransactionEntity() {
    }

    private TransactionEntity(long userId, TransactionInvoicingParty invoicingParty ,BigDecimal amount) {
        this.userId = userId;
        this.invoicingParty = invoicingParty;
        this.amount = amount;
    }

    public static TransactionEntity of(long userId, TransactionInvoicingParty invoicingParty, BigDecimal amount) {
        validateUserId(userId);
        validateInvoicingParty(invoicingParty);
        validateAmount(amount);
        return new TransactionEntity(userId, invoicingParty, amount);
    }

    private static void validateUserId(long userId) {
        if (userId <= 0) {
            throw new UserIdInputFormatInvalidException(TransactionErrorMessages.USER_ID_INPUT_NEGATIVE);
        }
    }

    public void updateUserId(long userId) {
        validateUserId(userId);
        this.userId = userId;
    }

    private static void validateAmount(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException(TransactionErrorMessages.AMOUNT_INPUT_NULL);
        }
    }

    public void updateAmount(BigDecimal amount) {
        validateAmount(amount);
        this.amount = amount;
    }

    private static void validateInvoicingParty(TransactionInvoicingParty invoicingParty) {
        if (invoicingParty == null) {
            throw new IllegalArgumentException(TransactionErrorMessages.INVOICING_PARTY_INPUT_NULL);
        }
    }

    public void updateInvoicingParty(TransactionInvoicingParty invoicingParty) {
        validateInvoicingParty(invoicingParty);
        this.invoicingParty = invoicingParty;
    }
        
}
