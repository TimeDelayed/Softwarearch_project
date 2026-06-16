package com.instantwin.bank.DTO.Transaction;

import java.math.BigDecimal;

import com.instantwin.bank.Utilities.Transaction.TransactionInvoicingParty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Data Transfer Object representing a transaction in the banking system.")
public record TransactionDTO(
        @NotNull @Schema(description = "Used in RequestBody of a transaction from either bank itself or other microservices", example = "235.13") BigDecimal amount,
        @NotNull @Schema(description = "Invoicing party issuing the transaction", example = "SLOTS") TransactionInvoicingParty invoicingParty) {

    BigDecimal getAmount() {
        return amount;
    }

    TransactionInvoicingParty getInvoicingParty() {
        return invoicingParty;
    }

}
