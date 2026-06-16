package com.instantwin.bank.DTO.Transaction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;

@Schema(description = "Data Transfer Object used to check if a user exists in the banking system, used in transaction requests.")
@JsonIgnoreProperties(ignoreUnknown = true)
public record TransactionRequestUser(@Positive @Schema(description = "Id of user inside transaction. Can't be null or < 1.", example = "123") long id) {
    long getId() {
        return id;
    }
}
