package com.instantwin.roulette.contract.handler;

import java.math.BigDecimal;
import java.util.List;

import com.instantwin.roulette.contract.view.IGameView;
import com.instantwin.roulette.game.BetType;

public interface IGameHandler {
    List<IGameView> findAllGames();
    IGameView play(BigDecimal betAmount, int betNumber, BetType betType);
}
