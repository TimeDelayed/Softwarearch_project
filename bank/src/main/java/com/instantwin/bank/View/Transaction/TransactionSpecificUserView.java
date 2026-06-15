package com.instantwin.bank.View.Transaction;

import java.math.BigDecimal;

import com.instantwin.bank.Model.Transaction.TransactionEntity;

import jakarta.validation.constraints.NotNull;

public record TransactionSpecificUserView(
        @NotNull long userId,
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
