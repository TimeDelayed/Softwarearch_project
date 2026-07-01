package com.instantwin.slotmachine.dto;

import java.math.BigDecimal;
import java.util.Arrays;

public record SlotGameResultDTO(BigDecimal betAmount, int[] spinResultNumbers, boolean won, BigDecimal winnings) {
    
    BigDecimal getBetAmount() {
        return betAmount;
    }
    int[] getSpinResultNumbers() {
        return Arrays.copyOf(spinResultNumbers, spinResultNumbers.length);
    }
    boolean getWon() {
        return won;
    }
    BigDecimal getWinnings() {
        return winnings;
    }
}
