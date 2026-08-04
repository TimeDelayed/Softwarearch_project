package com.instantwin.roulette.game;

import java.math.BigDecimal;
import java.security.SecureRandom;

import org.springframework.stereotype.Component;

import com.instantwin.roulette.contract.game.IRouletteGame;
import com.instantwin.roulette.utilities.InvalidBetException;
import com.instantwin.roulette.utilities.RouletteErrorMessages;

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
        validateBet(betAmount, betNumber, betType);

        int winningNumber = random.nextInt(37);
        BigDecimal payout = betType.isWinner(winningNumber, betNumber)
                ? betType.calculatePayout(betAmount)
                : BigDecimal.ZERO;
        return new GameResult(winningNumber, payout);
    }

    private void validateBet(BigDecimal betAmount, int betNumber, BetType betType) {
        if (betAmount == null || betAmount.signum() <= 0) {
            throw new InvalidBetException(RouletteErrorMessages.BET_AMOUNT_INVALID);
        }

        if (betType == null) {
            throw new InvalidBetException(RouletteErrorMessages.BET_TYPE_INVALID);
        }

        if (!betType.isValidBetNumber(betNumber)) {
            throw new InvalidBetException(RouletteErrorMessages.BET_NUMBER_INVALID);
        }
    }
}
