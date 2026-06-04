package com.instantwin.bank.DTO.User;

import java.math.BigDecimal;

import com.instantwin.bank.Utilities.InvoicingParty;

import jakarta.validation.constraints.NotNull;

public record UserRequestTransaction(@NotNull InvoicingParty invoicingParty, @NotNull BigDecimal amount){

    public BigDecimal getAmount() {
        return amount;
    }

    public InvoicingParty getInvoicingParty() {
        return invoicingParty;
    }
    
}
