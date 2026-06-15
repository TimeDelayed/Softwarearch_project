package com.instantwin.bank.DTO.User;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UserTransactionDTO(
        @NotNull BigDecimal amount) {

    public BigDecimal getAmount() {
        return amount;
    }
}
