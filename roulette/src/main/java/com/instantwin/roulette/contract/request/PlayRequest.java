package com.instantwin.roulette.contract.request;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.instantwin.roulette.game.BetType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "Request for placing and settling one roulette bet.")
public record PlayRequest(
        @Positive
        @Schema(description = "ID of the user placing the bet", example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @JsonProperty("user") long userId,

        @NotNull @Positive
        @Schema(description = "Amount wagered. Must be positive.", example = "10.00")
        @JsonProperty("betAmount") BigDecimal betAmount,

        @NotNull
        @Schema(description = "Bet number. Meaning depends on the bet type (e.g. the chosen number for STRAIGHT_UP, row index for STREET). Ignored for colour/parity bets.", example = "17")
        @JsonProperty("betNumber") Integer betNumber,

        @NotNull
        @Schema(description = "Bet type. Determines the covered numbers and the payout multiplier.", example = "STRAIGHT_UP")
        @JsonProperty("betType") BetType betType
) {}

