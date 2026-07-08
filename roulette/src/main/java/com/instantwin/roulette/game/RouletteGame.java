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

    private final SecureRandom random;

    public RouletteGame() {
        this.random = new SecureRandom();
    }

    // Package-private Konstruktor für Unit-Tests mit kontrollierbarem Random
    RouletteGame(SecureRandom random) {
        this.random = random;
    }

    public GameResult play(BigDecimal betAmount, int betNumber, BetType betType) {
        int winningNumber = random.nextInt(37);
        BigDecimal payout = betType.isWinner(winningNumber, betNumber)
                ? betType.calculatePayout(betAmount)
                : BigDecimal.ZERO;
        return new GameResult(winningNumber, payout);
    }
}
