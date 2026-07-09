package com.instantwin.slotmachine.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record SlotGameRequestBodyDTO(@Positive long userId, @NotNull @Positive BigDecimal betAmount) {

    public long getUserId() {
        return userId;
    }

    public BigDecimal getBetAmount() {
        return betAmount;
    }

}
