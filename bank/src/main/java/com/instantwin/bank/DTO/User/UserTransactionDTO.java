package com.instantwin.bank.DTO.User;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Data Transfer Object representing a transaction for a user in the banking system.")
@JsonIgnoreProperties(ignoreUnknown = true)
public record UserTransactionDTO(
        @NotNull @Schema(description = "Read transfer amount, summed with others and thus representing part of the users balance.", example = "100.23") BigDecimal amount) {

    public BigDecimal getAmount() {
        return amount;
    }
}
