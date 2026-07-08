package com.instantwin.roulette.game;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.security.SecureRandom;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RouletteGameTest {

    @Test
    void play_returnsPayout_whenBetWins() {
        SecureRandom mockRandom = mock(SecureRandom.class);
        when(mockRandom.nextInt(37)).thenReturn(17);

        RouletteGame game = new RouletteGame(mockRandom);
        GameResult result = game.play(new BigDecimal("10"), 17, BetType.STRAIGHT_UP);

        assertThat(result.winningNumber()).isEqualTo(17);
        assertThat(result.payout()).isEqualByComparingTo(new BigDecimal("350"));
    }

    @Test
    void play_returnsZeroPayout_whenBetLoses() {
        SecureRandom mockRandom = mock(SecureRandom.class);
        when(mockRandom.nextInt(37)).thenReturn(5);

        RouletteGame game = new RouletteGame(mockRandom);
        GameResult result = game.play(new BigDecimal("10"), 17, BetType.STRAIGHT_UP);

        assertThat(result.winningNumber()).isEqualTo(5);
        assertThat(result.payout()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void play_returnsRedPayout_whenWinningNumberIsRed() {
        SecureRandom mockRandom = mock(SecureRandom.class);
        when(mockRandom.nextInt(37)).thenReturn(1); // 1 ist rot

        RouletteGame game = new RouletteGame(mockRandom);
        GameResult result = game.play(new BigDecimal("10"), 0, BetType.RED);

        assertThat(result.payout()).isEqualByComparingTo(new BigDecimal("20"));
    }

    @Test
    void play_winningNumber_isAlwaysInValidRange() {
        RouletteGame game = new RouletteGame();
        for (int i = 0; i < 200; i++) {
            GameResult result = game.play(new BigDecimal("10"), 1, BetType.RED);
            assertThat(result.winningNumber())
                    .as("Gewinnzahl muss zwischen 0 und 36 liegen")
                    .isBetween(0, 36);
        }
    }

    @Test
    void play_payout_isNeverNegative() {
        RouletteGame game = new RouletteGame();
        for (int i = 0; i < 100; i++) {
            GameResult result = game.play(new BigDecimal("10"), 1, BetType.STRAIGHT_UP);
            assertThat(result.payout())
                    .as("Auszahlung darf nie negativ sein")
                    .isGreaterThanOrEqualTo(BigDecimal.ZERO);
        }
    }
}
