package com.instantwin.roulette.Model;

import org.junit.jupiter.api.Test;

import com.instantwin.roulette.game.BetType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class GameEntityTest {

    @Test
    void constructor_setsAllFieldsCorrectly() {
        BigDecimal betAmount = new BigDecimal("25.00");
        int betNumber = 7;
        BetType betType = BetType.STRAIGHT_UP;
        int winningNumber = 7;
        BigDecimal payout = new BigDecimal("875.00");

        GameEntity entity = new GameEntity(42L, betAmount, betNumber, betType, winningNumber, payout);

        assertThat(entity.getUserId()).isEqualTo(42L);
        assertThat(entity.getBetAmount()).isEqualByComparingTo(betAmount);
        assertThat(entity.getBetNumber()).isEqualTo(betNumber);
        assertThat(entity.getBetType()).isEqualTo(betType);
        assertThat(entity.getWinningNumber()).isEqualTo(winningNumber);
        assertThat(entity.getPayout()).isEqualByComparingTo(payout);
    }

    @Test
    void constructor_setsPlayedAtAutomatically() {
        LocalDateTime before = LocalDateTime.now().minusSeconds(1);
        GameEntity entity = new GameEntity(1L, BigDecimal.TEN, 0, BetType.RED, 3, BigDecimal.ZERO);
        LocalDateTime after = LocalDateTime.now().plusSeconds(1);

        assertThat(entity.getPlayedAt())
                .isNotNull()
                .isBetween(before, after);
    }

    @Test
    void id_isNullBeforePersistence() {
        GameEntity entity = new GameEntity(1L, BigDecimal.TEN, 0, BetType.RED, 3, BigDecimal.ZERO);

        assertThat(entity.getId()).isNull();
    }

    @Test
    void twoEntities_haveIndependentPlayedAtTimestamps() throws InterruptedException {
        GameEntity first = new GameEntity(1L, BigDecimal.TEN, 0, BetType.RED, 3, BigDecimal.ZERO);
        Thread.sleep(5);
        GameEntity second = new GameEntity(2L, BigDecimal.TEN, 0, BetType.BLACK, 4, BigDecimal.ZERO);

        assertThat(first.getPlayedAt()).isBefore(second.getPlayedAt());
    }
}
