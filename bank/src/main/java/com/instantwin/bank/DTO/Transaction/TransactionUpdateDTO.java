package com.instantwin.bank.DTO.Transaction;

import java.math.BigDecimal;

import com.instantwin.bank.utilities.Transaction.TransactionInvoicingParty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "Data Transfer Object representing an update to a transaction in the banking system.")
public record TransactionUpdateDTO(
        @Positive @Schema(description = "ID of user inside transaction. Can be updated but has to exist. Can't be null or < 1.", example = "123") long userId,
        @NotNull @Schema(description = "Amount to be updated.", example = "987.23") BigDecimal amount,
        @NotNull @Schema(description = "Invoicing party that issued the transaction to be updated.", example = "ROULETTE") TransactionInvoicingParty invoicingParty) {
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
