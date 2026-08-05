package com.instantwin.roulette.view;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

import com.instantwin.roulette.View.StatsView;
import com.instantwin.roulette.contract.view.IStatsView;

class StatsViewTest {

    @Test
    void of_mapsAllFieldsCorrectly() {
        IStatsView view = StatsView.of(
                5L,
                100L,
                new BigDecimal("200.00"),
                new BigDecimal("800.00"),
                new BigDecimal("1000.00")
        );

        assertThat(view.getTotalClientCount()).isEqualTo(5L);
        assertThat(view.getTotalGamesCount()).isEqualTo(100L);
        assertThat(view.getTotalProfit()).isEqualByComparingTo(new BigDecimal("200.00"));
        assertThat(view.getTotalCashOut()).isEqualByComparingTo(new BigDecimal("800.00"));
        assertThat(view.getTotalTurnover()).isEqualByComparingTo(new BigDecimal("1000.00"));
    }

    @Test
    void of_withZeroValues_returnsAllZeroes() {
        IStatsView view = StatsView.of(0L, 0L, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);

        assertThat(view.getTotalClientCount()).isZero();
        assertThat(view.getTotalGamesCount()).isZero();
        assertThat(view.getTotalProfit()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(view.getTotalCashOut()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(view.getTotalTurnover()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void of_withNegativeProfit_mapsCorrectly() {
        IStatsView view = StatsView.of(1L, 1L, new BigDecimal("-50.00"), new BigDecimal("150.00"), new BigDecimal("100.00"));

        assertThat(view.getTotalProfit()).isEqualByComparingTo(new BigDecimal("-50.00"));
    }
}
