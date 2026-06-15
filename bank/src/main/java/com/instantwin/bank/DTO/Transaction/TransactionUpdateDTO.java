package com.instantwin.bank.DTO.Transaction;

import java.math.BigDecimal;

import com.instantwin.bank.Utilities.Transaction.TransactionInvoicingParty;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TransactionUpdateDTO(@Positive long userId, @NotNull BigDecimal amount,
        @NotNull TransactionInvoicingParty invoicingParty) {
    long getUserId() {
        return userId;
    }

    BigDecimal getAmount() {
        return amount;
    }

    TransactionInvoicingParty getInvoicingParty() {
        return invoicingParty;
    }
}
