package com.instantwin.slotmachine.dto;

import java.math.BigDecimal;

public record SlotGameResultDTO(BigDecimal betAmount,
        ThreeReelSpinDTO spinResultSymbols, boolean won, BigDecimal winnings) {

    BigDecimal getBetAmount() {
        return betAmount;
    }

    ThreeReelSpinDTO getSpinResultNumbers() {
        return spinResultSymbols;
    }

    boolean getWon() {
        return won;
    }

    BigDecimal getWinnings() {
        return winnings;
    }
}
