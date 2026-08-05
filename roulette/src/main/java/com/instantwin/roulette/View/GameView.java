package com.instantwin.roulette.View;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.instantwin.roulette.contract.view.IGameView;
import com.instantwin.roulette.game.BetType;
import com.instantwin.roulette.model.GameEntity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Public representation of a settled roulette game.")
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
    @Schema(description = "ID of the stored game", example = "1")
    public Long getId() { return id; }

    @Override
    @JsonProperty("user")
    @Schema(description = "ID of the user who played the game", example = "1")
    public long getUserId() { return userId; }

    @Override
    @JsonProperty("betAmount")
    @Schema(description = "Amount wagered by the user", example = "10.00")
    public BigDecimal getBetAmount() { return betAmount; }

    @Override
    @JsonProperty("betNumber")
    @Schema(description = "Selected number or group index, depending on betType", example = "17")
    public int getBetNumber() { return betNumber; }

    @Override
    @JsonProperty("betType")
    @Schema(description = "Type of roulette bet", example = "STRAIGHT_UP")
    public BetType getBetType() { return betType; }

    @Override
    @JsonIgnore
    @Schema(hidden = true)
    public int getWinningNumber() { return winningNumber; }

    @Override
    @JsonIgnore
    @Schema(hidden = true)
    public BigDecimal getPayout() { return payout; }

    @Override
    @JsonProperty("winning")
    @Schema(description = "Whether the selected bet covered the winning number", example = "true")
    public boolean isWinning() {
        return payout.compareTo(BigDecimal.ZERO) > 0;
    }

    @Override
    @JsonProperty("amount")
    @Schema(description = "Net result of the game: payout minus bet amount", example = "350.00")
    public BigDecimal getAmount() {
        return payout.subtract(betAmount);
    }

    @JsonProperty("ballPosition")
    @Schema(description = "Number on which the roulette ball landed", example = "17")
    public int getBallPosition() { return winningNumber; }
}
