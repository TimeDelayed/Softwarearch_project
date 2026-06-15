package com.instantwin.bank.DTO.Transaction;

import java.math.BigDecimal;

import com.instantwin.bank.Utilities.Transaction.TransactionInvoicingParty;

import jakarta.validation.constraints.NotNull;

public record TransactionDTO(@NotNull BigDecimal amount, @NotNull TransactionInvoicingParty invoicingParty) {

    BigDecimal getAmount() {
        return amount;
    }

    TransactionInvoicingParty getInvoicingParty() {
        return invoicingParty;
    }

}
