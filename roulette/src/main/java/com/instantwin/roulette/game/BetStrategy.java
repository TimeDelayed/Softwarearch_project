package com.instantwin.roulette.game;

import java.math.BigDecimal;

public interface BetStrategy {
    boolean isWinner(int winningNumber, int betNumber);
    BigDecimal calculatePayout(BigDecimal betAmount);
}
