package com.instantwin.roulette.contract.view;

import java.math.BigDecimal;

import com.instantwin.roulette.game.BetType;

public interface IGameView {
    Long getId();
    BigDecimal getBetAmount();
    BetType getBetType();
    int getWinningNumber();
    BigDecimal getPayout();
}
