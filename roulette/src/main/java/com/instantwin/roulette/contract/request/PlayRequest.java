package com.instantwin.roulette.contract.request;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.instantwin.roulette.game.BetType;

public record PlayRequest(
        @JsonProperty("user") long userId,
        @JsonProperty("bet_amount") BigDecimal betAmount,
        @JsonProperty("bet_number") int betNumber,
        @JsonProperty("bet_type") BetType betType
) {}
