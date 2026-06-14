package com.instantwin.bank.View.Transaction;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;

public record TransactionView(
        @NotNull long id,
        @NotNull long userId,
        @NotNull BigDecimal amount) {

    public long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }
    
}
