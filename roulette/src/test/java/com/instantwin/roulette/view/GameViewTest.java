package com.instantwin.roulette.view;

import org.junit.jupiter.api.Test;

import com.instantwin.roulette.View.GameView;
import com.instantwin.roulette.contract.view.IGameView;
import com.instantwin.roulette.game.BetType;
import com.instantwin.roulette.model.GameEntity;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class GameViewTest {

    @Test
    void of_mapsAllEntityFieldsToView() {
        GameEntity entity = new GameEntity(
                1L,
                new BigDecimal("10.00"),
                7,
                BetType.STRAIGHT_UP,
                7,
                new BigDecimal("360.00")
        );

        IGameView view = GameView.of(entity);

        assertThat(view.getUserId()).isEqualTo(1L);
        assertThat(view.getBetAmount()).isEqualByComparingTo(new BigDecimal("10.00"));
        assertThat(view.getBetType()).isEqualTo(BetType.STRAIGHT_UP);
        assertThat(view.getWinningNumber()).isEqualTo(7);
        assertThat(view.getPayout()).isEqualByComparingTo(new BigDecimal("360.00"));
    }

    @Test
    void of_withZeroPayout_setsPayoutToZero() {
        GameEntity entity = new GameEntity(
                2L, new BigDecimal("10.00"), 3, BetType.RED, 2, BigDecimal.ZERO
        );

        IGameView view = GameView.of(entity);

        assertThat(view.getPayout()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(view.getBetType()).isEqualTo(BetType.RED);
    }

    @Test
    void of_idIsNull_whenEntityIsNotPersisted() {
        GameEntity entity = new GameEntity(
                3L, BigDecimal.TEN, 0, BetType.EVEN, 4, new BigDecimal("20.00")
        );

        IGameView view = GameView.of(entity);

        assertThat(view.getId()).isNull();
    }
}
