package com.instantwin.roulette.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.instantwin.roulette.game.BetType;
import com.instantwin.roulette.utilities.RouletteErrorMessages;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "games")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GameEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private long userId;

    @Column(nullable = false)
    private BigDecimal betAmount;

    @Column(nullable = false)
    private int betNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BetType betType;

    @Column(nullable = false)
    private int winningNumber;

    @Column(nullable = false)
    private BigDecimal payout;

    @Column(nullable = false)
    private LocalDateTime playedAt;

    public GameEntity(long userId, BigDecimal betAmount, int betNumber, BetType betType,
                      int winningNumber, BigDecimal payout) {
        validate(userId, betAmount, betNumber, betType, winningNumber, payout);
        this.userId = userId;
        this.betAmount = betAmount;
        this.betNumber = betNumber;
        this.betType = betType;
        this.winningNumber = winningNumber;
        this.payout = payout;
        this.playedAt = LocalDateTime.now();
    }

    private static void validate(long userId, BigDecimal betAmount, int betNumber, BetType betType,
                                 int winningNumber, BigDecimal payout) {
        if (userId <= 0) {
            throw new IllegalArgumentException(RouletteErrorMessages.USER_ID_INVALID);
        }
        if (betAmount == null || betAmount.signum() <= 0) {
            throw new IllegalArgumentException(RouletteErrorMessages.BET_AMOUNT_INVALID);
        }
        if (betType == null) {
            throw new IllegalArgumentException(RouletteErrorMessages.BET_TYPE_INVALID);
        }
        if (!betType.isValidBetNumber(betNumber)) {
            throw new IllegalArgumentException(RouletteErrorMessages.BET_NUMBER_INVALID);
        }
        if (winningNumber < 0 || winningNumber > 36) {
            throw new IllegalArgumentException(RouletteErrorMessages.WINNING_NUMBER_INVALID);
        }
        if (payout == null || payout.signum() < 0) {
            throw new IllegalArgumentException(RouletteErrorMessages.PAYOUT_INVALID);
        }

        BigDecimal expectedPayout = betType.isWinner(winningNumber, betNumber)
                ? betType.calculatePayout(betAmount)
                : BigDecimal.ZERO;
        if (payout.compareTo(expectedPayout) != 0) {
            throw new IllegalArgumentException(RouletteErrorMessages.GAME_RESULT_INVALID);
        }
    }
}
