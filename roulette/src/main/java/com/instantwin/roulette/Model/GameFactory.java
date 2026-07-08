package com.instantwin.roulette.Model;

import java.math.BigDecimal;

import com.instantwin.roulette.game.BetType;
import com.instantwin.roulette.game.GameResult;

/**
 * SRP: Factory ist ausschließlich für die Erzeugung von GameEntity zuständig.
 */
public class GameFactory {

    private GameFactory() {}

    public static GameEntity create(BigDecimal betAmount, int betNumber, BetType betType, GameResult result) {
        return new GameEntity(betAmount, betNumber, betType, result.winningNumber(), result.payout());
    }
}
