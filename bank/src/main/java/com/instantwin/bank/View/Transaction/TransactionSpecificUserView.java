package com.instantwin.bank.view.Transaction;

import java.math.BigDecimal;

import com.instantwin.bank.model.Transaction.TransactionEntity;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TransactionSpecificUserView(
        @Positive long id,
        @NotNull BigDecimal amount) {
    public static TransactionSpecificUserView of(TransactionEntity transactionEntity) {
        return new TransactionSpecificUserView(transactionEntity.getId(),
                transactionEntity.getAmount());
    }

    public long getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
