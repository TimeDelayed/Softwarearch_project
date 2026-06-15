package com.instantwin.bank.DTO.Transaction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TransactionRequestUser(@NotNull @Positive long id) {
    long getId() {
        return id;
    }
}
