package com.instantwin.roulette.game;

import java.math.BigDecimal;
import java.security.SecureRandom;

import org.springframework.stereotype.Component;

import com.instantwin.roulette.contract.game.IRouletteGame;

@Component
public class RouletteGame implements IRouletteGame {

    private final SecureRandom random;

    public RouletteGame() {
        this.random = new SecureRandom();
    }

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
