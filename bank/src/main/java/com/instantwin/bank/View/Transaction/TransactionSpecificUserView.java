package com.instantwin.bank.View.Transaction;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;

public record TransactionSpecificUserView(
        @NotNull long id,
        @NotNull BigDecimal amount
) {
    
    public long getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
