package com.instantwin.bank.DTO.User;

import java.math.BigDecimal;

import com.instantwin.bank.utilities.User.UserInvoicingParty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Data Transfer Object representing a transaction request for a user in the banking system.")
public record UserRequestTransaction(
        @NotNull @Schema(description = "Invoicing party of request.", example = "USER_SLICE") UserInvoicingParty invoicingParty,
        @NotNull @Schema(description = "Amount to be transferred.", example = "-100") BigDecimal amount) {

    public BigDecimal getAmount() {
        return amount;
    }

    public UserInvoicingParty getInvoicingParty() {
        return invoicingParty;
    }

}
