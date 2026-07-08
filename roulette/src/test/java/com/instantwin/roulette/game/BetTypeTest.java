package com.instantwin.roulette.game;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class BetTypeTest {

    // -------------------------------------------------------------------------
    // STRAIGHT_UP
    // -------------------------------------------------------------------------

    @Test
    void straightUp_wins_whenBetNumberMatchesWinningNumber() {
        assertThat(BetType.STRAIGHT_UP.isWinner(17, 17)).isTrue();
    }

    @Test
    void straightUp_loses_whenNumbersDiffer() {
        assertThat(BetType.STRAIGHT_UP.isWinner(17, 5)).isFalse();
    }

    @Test
    void straightUp_payout_is35x() {
        assertThat(BetType.STRAIGHT_UP.calculatePayout(new BigDecimal("10")))
                .isEqualByComparingTo(new BigDecimal("350"));
    }

    // -------------------------------------------------------------------------
    // RED
    // -------------------------------------------------------------------------

    @ParameterizedTest
    @ValueSource(ints = {1, 3, 5, 7, 9, 12, 14, 16, 18, 19, 21, 23, 25, 27, 30, 32, 34, 36})
    void red_wins_forEveryRedNumber(int redNumber) {
        assertThat(BetType.RED.isWinner(redNumber, 0)).isTrue();
    }

    @Test
    void red_loses_forZero() {
        assertThat(BetType.RED.isWinner(0, 0)).isFalse();
    }

    @Test
    void red_loses_forBlackNumber() {
        assertThat(BetType.RED.isWinner(2, 0)).isFalse();
    }

    @Test
    void red_payout_is2x() {
        assertThat(BetType.RED.calculatePayout(new BigDecimal("10")))
                .isEqualByComparingTo(new BigDecimal("20"));
    }

    // -------------------------------------------------------------------------
    // BLACK
    // -------------------------------------------------------------------------

    @ParameterizedTest
    @ValueSource(ints = {2, 4, 6, 8, 10, 11, 13, 15, 17, 20, 22, 24, 26, 28, 29, 31, 33, 35})
    void black_wins_forEveryBlackNumber(int blackNumber) {
        assertThat(BetType.BLACK.isWinner(blackNumber, 0)).isTrue();
    }

    @Test
    void black_loses_forZero() {
        assertThat(BetType.BLACK.isWinner(0, 0)).isFalse();
    }

    @Test
    void black_loses_forRedNumber() {
        assertThat(BetType.BLACK.isWinner(1, 0)).isFalse();
    }

    // -------------------------------------------------------------------------
    // EVEN
    // -------------------------------------------------------------------------

    @ParameterizedTest
    @ValueSource(ints = {2, 4, 6, 8, 10, 34, 36})
    void even_wins_forEvenNumbers(int n) {
        assertThat(BetType.EVEN.isWinner(n, 0)).isTrue();
    }

    @Test
    void even_loses_forZero() {
        assertThat(BetType.EVEN.isWinner(0, 0)).isFalse();
    }

    @Test
    void even_loses_forOddNumber() {
        assertThat(BetType.EVEN.isWinner(7, 0)).isFalse();
    }

    @Test
    void even_payout_is2x() {
        assertThat(BetType.EVEN.calculatePayout(new BigDecimal("10")))
                .isEqualByComparingTo(new BigDecimal("20"));
    }

    // -------------------------------------------------------------------------
    // ODD
    // -------------------------------------------------------------------------

    @ParameterizedTest
    @ValueSource(ints = {1, 3, 5, 7, 33, 35})
    void odd_wins_forOddNumbers(int n) {
        assertThat(BetType.ODD.isWinner(n, 0)).isTrue();
    }

    @Test
    void odd_loses_forZero() {
        assertThat(BetType.ODD.isWinner(0, 0)).isFalse();
    }

    @Test
    void odd_loses_forEvenNumber() {
        assertThat(BetType.ODD.isWinner(4, 0)).isFalse();
    }

    // -------------------------------------------------------------------------
    // SPLIT  (betNumber > 0 = vertikal, betNumber < 0 = horizontal)
    // -------------------------------------------------------------------------

    @Test
    void split_vertical_wins_forBothNumbers() {
        // betNumber=5 (positiv) → deckt 5 und 8 ab
        assertThat(BetType.SPLIT.isWinner(5, 5)).isTrue();
        assertThat(BetType.SPLIT.isWinner(8, 5)).isTrue();
    }

    @Test
    void split_vertical_loses_forOtherNumber() {
        assertThat(BetType.SPLIT.isWinner(6, 5)).isFalse();
    }

    @Test
    void split_horizontal_wins_forBothNumbers() {
        // betNumber=-4 (negativ) → deckt 4 und 5 ab
        assertThat(BetType.SPLIT.isWinner(4, -4)).isTrue();
        assertThat(BetType.SPLIT.isWinner(5, -4)).isTrue();
    }

    @Test
    void split_loses_forZeroWinningNumber() {
        assertThat(BetType.SPLIT.isWinner(0, 5)).isFalse();
    }

    @Test
    void split_payout_is17x() {
        assertThat(BetType.SPLIT.calculatePayout(new BigDecimal("10")))
                .isEqualByComparingTo(new BigDecimal("170"));
    }

    // -------------------------------------------------------------------------
    // STREET  (betNumber = Reihenindex 1..12)
    // -------------------------------------------------------------------------

    @Test
    void street_wins_forAllThreeNumbersInRow() {
        // Reihe 1 → 1, 2, 3
        assertThat(BetType.STREET.isWinner(1, 1)).isTrue();
        assertThat(BetType.STREET.isWinner(2, 1)).isTrue();
        assertThat(BetType.STREET.isWinner(3, 1)).isTrue();
    }

    @Test
    void street_loses_forNumberOutsideRow() {
        assertThat(BetType.STREET.isWinner(4, 1)).isFalse();
    }

    @Test
    void street_loses_forZero() {
        assertThat(BetType.STREET.isWinner(0, 1)).isFalse();
    }

    @Test
    void street_payout_is11x() {
        assertThat(BetType.STREET.calculatePayout(new BigDecimal("10")))
                .isEqualByComparingTo(new BigDecimal("110"));
    }

    // -------------------------------------------------------------------------
    // CORNER  (betNumber = oben-links-Zahl des 2×2-Blocks)
    // -------------------------------------------------------------------------

    @Test
    void corner_wins_forAllFourBlockNumbers() {
        // betNumber=1 → deckt 1, 2, 4, 5 ab
        assertThat(BetType.CORNER.isWinner(1, 1)).isTrue();
        assertThat(BetType.CORNER.isWinner(2, 1)).isTrue();
        assertThat(BetType.CORNER.isWinner(4, 1)).isTrue();
        assertThat(BetType.CORNER.isWinner(5, 1)).isTrue();
    }

    @Test
    void corner_loses_forZero() {
        assertThat(BetType.CORNER.isWinner(0, 1)).isFalse();
    }

    @Test
    void corner_loses_forNumberOutsideBlock() {
        assertThat(BetType.CORNER.isWinner(6, 1)).isFalse();
    }

    @Test
    void corner_payout_is8x() {
        assertThat(BetType.CORNER.calculatePayout(new BigDecimal("10")))
                .isEqualByComparingTo(new BigDecimal("80"));
    }

    // -------------------------------------------------------------------------
    // LINE  (betNumber = erster Reihenindex 1..11)
    // -------------------------------------------------------------------------

    @Test
    void line_wins_forAllSixNumbersInTwoRows() {
        // betNumber=1 → deckt 1, 2, 3, 4, 5, 6 ab
        assertThat(BetType.LINE.isWinner(1, 1)).isTrue();
        assertThat(BetType.LINE.isWinner(6, 1)).isTrue();
    }

    @Test
    void line_loses_forSeventhNumber() {
        assertThat(BetType.LINE.isWinner(7, 1)).isFalse();
    }

    @Test
    void line_payout_is5x() {
        assertThat(BetType.LINE.calculatePayout(new BigDecimal("10")))
                .isEqualByComparingTo(new BigDecimal("50"));
    }

    // -------------------------------------------------------------------------
    // DOZEN  (betNumber: 1→1-12, 2→13-24, 3→25-36)
    // -------------------------------------------------------------------------

    @Test
    void dozen_firstGroup_wins_for1to12() {
        assertThat(BetType.DOZEN.isWinner(1, 1)).isTrue();
        assertThat(BetType.DOZEN.isWinner(12, 1)).isTrue();
    }

    @Test
    void dozen_secondGroup_wins_for13to24() {
        assertThat(BetType.DOZEN.isWinner(13, 2)).isTrue();
        assertThat(BetType.DOZEN.isWinner(24, 2)).isTrue();
    }

    @Test
    void dozen_thirdGroup_wins_for25to36() {
        assertThat(BetType.DOZEN.isWinner(25, 3)).isTrue();
        assertThat(BetType.DOZEN.isWinner(36, 3)).isTrue();
    }

    @Test
    void dozen_loses_forZero() {
        assertThat(BetType.DOZEN.isWinner(0, 1)).isFalse();
    }

    @Test
    void dozen_loses_whenNumberIsInWrongGroup() {
        assertThat(BetType.DOZEN.isWinner(13, 1)).isFalse();
    }

    @Test
    void dozen_payout_is3x() {
        assertThat(BetType.DOZEN.calculatePayout(new BigDecimal("10")))
                .isEqualByComparingTo(new BigDecimal("30"));
    }

    // -------------------------------------------------------------------------
    // COLUMN  (betNumber: 1..3)
    // -------------------------------------------------------------------------

    @Test
    void column_first_wins_forFirstColumnNumbers() {
        // Spalte 1: 1, 4, 7, 10, 13, 16, 19, 22, 25, 28, 31, 34
        assertThat(BetType.COLUMN.isWinner(1, 1)).isTrue();
        assertThat(BetType.COLUMN.isWinner(4, 1)).isTrue();
        assertThat(BetType.COLUMN.isWinner(34, 1)).isTrue();
    }

    @Test
    void column_loses_forZero() {
        assertThat(BetType.COLUMN.isWinner(0, 1)).isFalse();
    }

    @Test
    void column_loses_whenNumberIsInWrongColumn() {
        // 2 gehört zu Spalte 2, nicht 1
        assertThat(BetType.COLUMN.isWinner(2, 1)).isFalse();
    }

    @Test
    void column_payout_is3x() {
        assertThat(BetType.COLUMN.calculatePayout(new BigDecimal("10")))
                .isEqualByComparingTo(new BigDecimal("30"));
    }
}
