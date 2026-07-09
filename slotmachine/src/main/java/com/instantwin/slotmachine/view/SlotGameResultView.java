package com.instantwin.slotmachine.view;

import java.math.BigDecimal;

import com.instantwin.slotmachine.dto.ThreeReelSpinDTO;

public record SlotGameResultView(BigDecimal betAmount,
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
