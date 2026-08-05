package com.instantwin.roulette.model;

import org.junit.jupiter.api.Test;

import com.instantwin.roulette.game.BetType;
import com.instantwin.roulette.game.GameResult;
import com.instantwin.roulette.model.GameEntity;
import com.instantwin.roulette.model.GameFactory;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class GameFactoryTest {

    private final GameFactory gameFactory = new GameFactory();

    @Test
    void create_returnsEntityWithAllFieldsMappedFromResult() {
        BigDecimal betAmount = new BigDecimal("10.00");
        int betNumber = 5;
        BetType betType = BetType.STRAIGHT_UP;
        GameResult result = new GameResult(5, new BigDecimal("360.00"));

        GameEntity entity = gameFactory.create(7L, betAmount, betNumber, betType, result);

        assertThat(entity.getUserId()).isEqualTo(7L);
        assertThat(entity.getBetAmount()).isEqualByComparingTo(betAmount);
        assertThat(entity.getBetNumber()).isEqualTo(betNumber);
        assertThat(entity.getBetType()).isEqualTo(betType);
        assertThat(entity.getWinningNumber()).isEqualTo(result.winningNumber());
        assertThat(entity.getPayout()).isEqualByComparingTo(result.payout());
    }

    @Test
    void create_withZeroPayout_setsPayoutToZero() {
        GameResult lostResult = new GameResult(10, BigDecimal.ZERO);

        GameEntity entity = gameFactory.create(3L, new BigDecimal("10"), 7, BetType.RED, lostResult);

        assertThat(entity.getPayout()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(entity.getWinningNumber()).isEqualTo(10);
    }
}
