package com.instantwin.bank.DTO.User;

import java.math.BigDecimal;

import com.instantwin.bank.contract.DTO.IUserRequestTransaction;

import jakarta.validation.constraints.NotNull;

public record UserRequestTransaction(@NotNull BigDecimal amount) implements IUserRequestTransaction {

    @Override
    public BigDecimal getAmount() {
        return amount;
    }
    
}
