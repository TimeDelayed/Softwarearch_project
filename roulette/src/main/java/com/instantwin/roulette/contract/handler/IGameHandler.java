package com.instantwin.roulette.contract.handler;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.instantwin.roulette.contract.view.IGameView;
import com.instantwin.roulette.contract.view.IStatsView;
import com.instantwin.roulette.contract.view.IUserStatsView;
import com.instantwin.roulette.game.BetType;

public interface IGameHandler {
    List<IGameView> findAllGames();
    Optional<IGameView> findGameById(long id);
    Optional<IGameView> deleteGame(long id);
    Optional<IGameView> play(long userId, BigDecimal betAmount, int betNumber, BetType betType);
    IStatsView getStats();
    Optional<IUserStatsView> getUserStats(long userId);
    String getRules();
    String getChances();
}
