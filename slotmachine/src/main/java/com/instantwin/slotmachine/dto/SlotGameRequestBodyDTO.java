package com.instantwin.slotmachine.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "Request for playing one slot game.")
public record SlotGameRequestBodyDTO(
        @Positive @Schema(description = "ID of the user placing the bet", example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED) long userId,
        @NotNull @Positive @Schema(description = "Amount wagered by the user. Must be positive.", example = "10.00") BigDecimal betAmount) {

    public long getUserId() {
        return userId;
    }

    public BigDecimal getBetAmount() {
        return betAmount;
    }

}
