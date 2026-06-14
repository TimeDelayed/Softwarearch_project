package com.instantwin.bank.DTO.User;

import java.math.BigDecimal;

import com.instantwin.bank.Utilities.User.UserInvoicingParty;

import jakarta.validation.constraints.NotNull;

public record UserRequestTransaction(@NotNull UserInvoicingParty invoicingParty, @NotNull BigDecimal amount){

    public BigDecimal getAmount() {
        return amount;
    }

    public UserInvoicingParty getInvoicingParty() {
        return invoicingParty;
    }
    
}
