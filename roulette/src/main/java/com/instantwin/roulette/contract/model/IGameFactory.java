package com.instantwin.roulette.contract.model;

import java.math.BigDecimal;

import com.instantwin.roulette.game.BetType;
import com.instantwin.roulette.game.GameResult;
import com.instantwin.roulette.model.GameEntity;

public interface IGameFactory {

    GameEntity create(long userId, BigDecimal betAmount, int betNumber,
                      BetType betType, GameResult result);
}
