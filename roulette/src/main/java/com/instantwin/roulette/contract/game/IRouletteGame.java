package com.instantwin.roulette.contract.game;

import java.math.BigDecimal;

import com.instantwin.roulette.game.BetType;
import com.instantwin.roulette.game.GameResult;

public interface IRouletteGame {
    GameResult play(BigDecimal betAmount, int betNumber, BetType betType);
}
