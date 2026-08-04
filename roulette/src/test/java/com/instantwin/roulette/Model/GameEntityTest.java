package com.instantwin.roulette.model;

import org.junit.jupiter.api.Test;

import com.instantwin.roulette.game.BetType;
import com.instantwin.roulette.model.GameEntity;
import com.instantwin.roulette.utilities.RouletteErrorMessages;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GameEntityTest {

    @Test
    void constructor_setsAllFieldsCorrectly() {
        BigDecimal betAmount = new BigDecimal("25.00");
        int betNumber = 7;
        BetType betType = BetType.STRAIGHT_UP;
        int winningNumber = 7;
        BigDecimal payout = new BigDecimal("900.00");

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
        GameEntity entity = new GameEntity(1L, BigDecimal.TEN, 0, BetType.RED, 2, BigDecimal.ZERO);
        LocalDateTime after = LocalDateTime.now().plusSeconds(1);

        assertThat(entity.getPlayedAt())
                .isNotNull()
                .isBetween(before, after);
    }

    @Test
    void id_isNullBeforePersistence() {
        GameEntity entity = new GameEntity(1L, BigDecimal.TEN, 0, BetType.RED, 2, BigDecimal.ZERO);

        assertThat(entity.getId()).isNull();
    }

    @Test
    void twoEntities_haveIndependentPlayedAtTimestamps() throws InterruptedException {
        GameEntity first = new GameEntity(1L, BigDecimal.TEN, 0, BetType.RED, 2, BigDecimal.ZERO);
        Thread.sleep(5);
        GameEntity second = new GameEntity(2L, BigDecimal.TEN, 0, BetType.BLACK, 1, BigDecimal.ZERO);

        assertThat(first.getPlayedAt()).isBefore(second.getPlayedAt());
    }

    @Test
    void constructor_rejectsInvalidUserId() {
        assertThatThrownBy(() -> new GameEntity(0L, BigDecimal.TEN, 0, BetType.RED, 2, BigDecimal.ZERO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(RouletteErrorMessages.USER_ID_INVALID);
    }

    @Test
    void constructor_rejectsInvalidBetAmount() {
        assertThatThrownBy(() -> new GameEntity(1L, BigDecimal.ZERO, 0, BetType.RED, 2, BigDecimal.ZERO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(RouletteErrorMessages.BET_AMOUNT_INVALID);
    }

    @Test
    void constructor_rejectsNullBetAmount() {
        assertThatThrownBy(() -> new GameEntity(1L, null, 0, BetType.RED, 2, BigDecimal.ZERO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(RouletteErrorMessages.BET_AMOUNT_INVALID);
    }

    @Test
    void constructor_rejectsNullBetType() {
        assertThatThrownBy(() -> new GameEntity(1L, BigDecimal.TEN, 0, null, 2, BigDecimal.ZERO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(RouletteErrorMessages.BET_TYPE_INVALID);
    }

    @Test
    void constructor_rejectsInvalidBetNumber() {
        assertThatThrownBy(() -> new GameEntity(1L, BigDecimal.TEN, 37, BetType.STRAIGHT_UP, 2, BigDecimal.ZERO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(RouletteErrorMessages.BET_NUMBER_INVALID);
    }

    @Test
    void constructor_rejectsInvalidWinningNumber() {
        assertThatThrownBy(() -> new GameEntity(1L, BigDecimal.TEN, 0, BetType.RED, 37, BigDecimal.ZERO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(RouletteErrorMessages.WINNING_NUMBER_INVALID);
    }

    @Test
    void constructor_rejectsInvalidPayout() {
        assertThatThrownBy(() -> new GameEntity(1L, BigDecimal.TEN, 0, BetType.RED, 2, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(RouletteErrorMessages.PAYOUT_INVALID);
    }

    @Test
    void constructor_rejectsNegativePayout() {
        assertThatThrownBy(() -> new GameEntity(1L, BigDecimal.TEN, 0, BetType.RED, 2, BigDecimal.ONE.negate()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(RouletteErrorMessages.PAYOUT_INVALID);
    }

    @Test
    void constructor_rejectsPayoutThatDoesNotMatchResult() {
        assertThatThrownBy(() -> new GameEntity(1L, BigDecimal.TEN, 0, BetType.RED, 3, BigDecimal.ZERO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(RouletteErrorMessages.GAME_RESULT_INVALID);
    }
}
