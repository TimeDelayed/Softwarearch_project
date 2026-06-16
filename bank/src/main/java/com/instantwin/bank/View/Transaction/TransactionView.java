package com.instantwin.bank.View.Transaction;

import java.math.BigDecimal;

import com.instantwin.bank.Model.Transaction.TransactionEntity;
import com.instantwin.bank.Utilities.Transaction.TransactionInvoicingParty;

import jakarta.validation.constraints.NotNull;

public record TransactionView(
        @NotNull long id,
        @NotNull long userId,
        @NotNull BigDecimal amount,
        @NotNull TransactionInvoicingParty invoicingParty) {

        
    public static TransactionView of(TransactionEntity transactionEntity) {
        return new TransactionView(transactionEntity.getId(), transactionEntity.getUserId(),
                transactionEntity.getAmount(), transactionEntity.getInvoicingParty());
    } 

    public long getId() {
        return id;
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
