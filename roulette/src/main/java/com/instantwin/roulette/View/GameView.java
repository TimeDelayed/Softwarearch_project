package com.instantwin.roulette.View;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.instantwin.roulette.Model.GameEntity;
import com.instantwin.roulette.contract.view.IGameView;
import com.instantwin.roulette.game.BetType;

/**
 * ISP: Implementiert IGameView vollständig – kein aufgeblähtes Interface.
 * SRP: Nur verantwortlich für die Darstellung einer Spielrunde.
 */
public class GameView implements IGameView {

    private final Long id;
    private final long userId;
    private final BigDecimal betAmount;
    private final int betNumber;
    private final BetType betType;
    private final int winningNumber;
    private final BigDecimal payout;

    private GameView(Long id, long userId, BigDecimal betAmount, int betNumber,
                     BetType betType, int winningNumber, BigDecimal payout) {
        this.id = id;
        this.userId = userId;
        this.betAmount = betAmount;
        this.betNumber = betNumber;
        this.betType = betType;
        this.winningNumber = winningNumber;
        this.payout = payout;
    }

    public static GameView of(GameEntity entity) {
        return new GameView(
                entity.getId(),
                entity.getUserId(),
                entity.getBetAmount(),
                entity.getBetNumber(),
                entity.getBetType(),
                entity.getWinningNumber(),
                entity.getPayout()
        );
    }

    @Override
    @JsonProperty("id")
    public Long getId() { return id; }

    @Override
    @JsonProperty("user")
    public long getUserId() { return userId; }

    @Override
    @JsonProperty("bet_amount")
    public BigDecimal getBetAmount() { return betAmount; }

    @Override
    @JsonProperty("bet_number")
    public int getBetNumber() { return betNumber; }

    @Override
    @JsonProperty("bet_type")
    public BetType getBetType() { return betType; }

    @Override
    @JsonIgnore
    public int getWinningNumber() { return winningNumber; }

    @Override
    @JsonIgnore
    public BigDecimal getPayout() { return payout; }

    @Override
    @JsonProperty("winning")
    public boolean isWinning() {
        return payout.compareTo(BigDecimal.ZERO) > 0;
    }

    @Override
    @JsonProperty("amount")
    public BigDecimal getAmount() {
        return payout.subtract(betAmount);
    }

    @JsonProperty("ball_position")
    public int getBallPosition() { return winningNumber; }
}
