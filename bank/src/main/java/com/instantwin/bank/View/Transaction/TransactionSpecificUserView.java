package com.instantwin.bank.view.Transaction;

import java.math.BigDecimal;

import com.instantwin.bank.model.Transaction.TransactionEntity;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TransactionSpecificUserView(
        @Positive long userId,
        @NotNull BigDecimal amount) {
    public static TransactionSpecificUserView of(TransactionEntity transactionEntity) {
        return new TransactionSpecificUserView(transactionEntity.getUserId(),
                transactionEntity.getAmount());
    }

    public long getUserId() {
        return userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
