package com.instantwin.roulette.Model;

import java.math.BigDecimal;

import com.instantwin.roulette.game.BetType;
import com.instantwin.roulette.game.GameResult;

public class GameFactory {

    private GameFactory() {}

    public static GameEntity create(long userId, BigDecimal betAmount, int betNumber,
                                    BetType betType, GameResult result) {
        return new GameEntity(userId, betAmount, betNumber, betType,
                result.winningNumber(), result.payout());
    }
}
