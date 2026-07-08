package com.instantwin.roulette.game;

import java.math.BigDecimal;
import java.security.SecureRandom;

import org.springframework.stereotype.Component;

/**
 * SRP: Nur verantwortlich für die Spiellogik – kein State, kein Persistence.
 * DIP: Als Spring-Component über Interface injizierbar.
 */
@Component
public class RouletteGame {

    private static final SecureRandom RANDOM = new SecureRandom();

    public GameResult play(BigDecimal betAmount, int betNumber, BetType betType) {
        int winningNumber = RANDOM.nextInt(37);
        BigDecimal payout = betType.isWinner(winningNumber, betNumber)
                ? betType.calculatePayout(betAmount)
                : BigDecimal.ZERO;
        return new GameResult(winningNumber, payout);
    }
}
