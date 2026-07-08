package com.instantwin.roulette.View;

import java.math.BigDecimal;

import com.instantwin.roulette.Model.GameEntity;
import com.instantwin.roulette.contract.view.IGameView;
import com.instantwin.roulette.game.BetType;

import lombok.Getter;

/**
 * ISP: Implementiert IGameView vollständig – kein aufgeblähtes Interface.
 * SRP: Nur verantwortlich für die Darstellung einer Spielrunde.
 */
@Getter
public class GameView implements IGameView {

    private final Long id;
    private final BigDecimal betAmount;
    private final BetType betType;
    private final int winningNumber;
    private final BigDecimal payout;

    private GameView(Long id, BigDecimal betAmount, BetType betType, int winningNumber, BigDecimal payout) {
        this.id = id;
        this.betAmount = betAmount;
        this.betType = betType;
        this.winningNumber = winningNumber;
        this.payout = payout;
    }

    public static GameView of(GameEntity entity) {
        return new GameView(
                entity.getId(),
                entity.getBetAmount(),
                entity.getBetType(),
                entity.getWinningNumber(),
                entity.getPayout()
        );
    }
}
