package com.instantwin.roulette.model;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.instantwin.roulette.contract.model.IGameFactory;
import com.instantwin.roulette.game.BetType;
import com.instantwin.roulette.game.GameResult;

@Component
public class GameFactory implements IGameFactory {

    @Override
    public GameEntity create(long userId, BigDecimal betAmount, int betNumber,
                             BetType betType, GameResult result) {
        return new GameEntity(userId, betAmount, betNumber, betType,
                result.winningNumber(), result.payout());
    }
}
