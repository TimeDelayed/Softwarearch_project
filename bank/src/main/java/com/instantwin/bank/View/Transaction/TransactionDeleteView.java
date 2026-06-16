package com.instantwin.bank.View.Transaction;

import java.math.BigDecimal;

import com.instantwin.bank.Model.Transaction.TransactionEntity;
import com.instantwin.bank.Utilities.Transaction.TransactionInvoicingParty;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TransactionDeleteView(@Positive long userId, @NotNull BigDecimal amount, @NotNull TransactionInvoicingParty invoicingParty) {

    public static TransactionDeleteView of(TransactionEntity transactionEntity) {
        return new TransactionDeleteView(transactionEntity.getUserId(),
                transactionEntity.getAmount(), transactionEntity.getInvoicingParty());
    }
    public long getUserId() {
        return userId;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public TransactionInvoicingParty getInvoicingParty() {
        return invoicingParty;
    }
    
}
