package com.instantwin.roulette.view;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

import com.instantwin.roulette.View.UserStatsView;
import com.instantwin.roulette.contract.view.IUserStatsView;

class UserStatsViewTest {

    @Test
    void of_mapsAllFieldsCorrectly() {
        IUserStatsView view = UserStatsView.of(
                42L,
                10L,
                new BigDecimal("500.00"),
                new BigDecimal("100.00"),
                new BigDecimal("400.00"),
                new BigDecimal("600.00"),
                new BigDecimal("200.00")
        );

        assertThat(view.getClient()).isEqualTo(42L);
        assertThat(view.getTotalGamesCount()).isEqualTo(10L);
        assertThat(view.getTotalWinnings()).isEqualByComparingTo(new BigDecimal("500.00"));
        assertThat(view.getTotalLosses()).isEqualByComparingTo(new BigDecimal("100.00"));
        assertThat(view.getTotalClientProfit()).isEqualByComparingTo(new BigDecimal("400.00"));
        assertThat(view.getTotalHouseTurnoverFromClient()).isEqualByComparingTo(new BigDecimal("600.00"));
        assertThat(view.getTotalHouseProfitFromClient()).isEqualByComparingTo(new BigDecimal("200.00"));
    }

    @Test
    void of_withZeroWinnings_setsWinningsToZero() {
        IUserStatsView view = UserStatsView.of(
                1L, 5L,
                BigDecimal.ZERO,
                new BigDecimal("50.00"),
                new BigDecimal("-50.00"),
                new BigDecimal("50.00"),
                new BigDecimal("50.00")
        );

        assertThat(view.getTotalWinnings()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(view.getTotalLosses()).isEqualByComparingTo(new BigDecimal("50.00"));
        assertThat(view.getTotalClientProfit()).isEqualByComparingTo(new BigDecimal("-50.00"));
    }

    @Test
    void of_clientIdIsPreserved() {
        IUserStatsView view = UserStatsView.of(
                99L, 1L,
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO
        );

        assertThat(view.getClient()).isEqualTo(99L);
    }
}
