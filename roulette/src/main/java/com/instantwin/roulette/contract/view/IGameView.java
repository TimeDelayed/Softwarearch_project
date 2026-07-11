package com.instantwin.roulette.contract.view;

import java.math.BigDecimal;

import com.instantwin.roulette.game.BetType;

public interface IGameView {
    Long getId();
    long getUserId();
    BigDecimal getBetAmount();
    int getBetNumber();
    BetType getBetType();
    int getWinningNumber();
    BigDecimal getPayout();
    boolean isWinning();
    BigDecimal getAmount();
}
