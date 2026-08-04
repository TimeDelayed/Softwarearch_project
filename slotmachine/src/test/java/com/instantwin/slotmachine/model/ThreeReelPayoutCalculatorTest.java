package com.instantwin.slotmachine.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.instantwin.slotmachine.contract.model.ISlotPayoutConfiguration;
import com.instantwin.slotmachine.dto.ThreeReelSpinDTO;
import com.instantwin.slotmachine.utilities.SlotBetMultipliers;
import com.instantwin.slotmachine.utilities.SlotSymbols;

public class ThreeReelPayoutCalculatorTest {

    private ThreeReelPayoutCalculator calculator;

    @BeforeEach
    void setUp() {
        ISlotPayoutConfiguration config = mock(ISlotPayoutConfiguration.class);

        when(config.getDoubleHitReferenceMap()).thenReturn(Map.of(
                SlotSymbols.CHERRY, SlotBetMultipliers.DOUBLE_CHERRY,
                SlotSymbols.LEMON, SlotBetMultipliers.DOUBLE_LEMON,
                SlotSymbols.BELL, SlotBetMultipliers.DOUBLE_BELL,
                SlotSymbols.DIAMOND, SlotBetMultipliers.DOUBLE_DIAMOND));

        when(config.getTripleHitReferenceMap()).thenReturn(Map.of(
                SlotSymbols.CHERRY, SlotBetMultipliers.TRIPLE_CHERRY,
                SlotSymbols.LEMON, SlotBetMultipliers.TRIPLE_LEMON,
                SlotSymbols.BELL, SlotBetMultipliers.TRIPLE_BELL,
                SlotSymbols.DIAMOND, SlotBetMultipliers.TRIPLE_DIAMOND));

        when(config.getSymbolMultipliers()).thenReturn(Map.of(
                SlotBetMultipliers.NO_MULTIPLIER, new BigDecimal("0"),
                SlotBetMultipliers.DOUBLE_CHERRY, new BigDecimal("0.80"),
                SlotBetMultipliers.TRIPLE_CHERRY, new BigDecimal("0.99"),
                SlotBetMultipliers.DOUBLE_LEMON, new BigDecimal("1.15"),
                SlotBetMultipliers.TRIPLE_LEMON, new BigDecimal("2.50"),
                SlotBetMultipliers.DOUBLE_BELL, new BigDecimal("1.70"),
                SlotBetMultipliers.TRIPLE_BELL, new BigDecimal("5.75"),
                SlotBetMultipliers.SINGLE_DIAMOND, new BigDecimal("1.50"),
                SlotBetMultipliers.DOUBLE_DIAMOND, new BigDecimal("15.80"),
                SlotBetMultipliers.TRIPLE_DIAMOND, new BigDecimal("498.00")));

        calculator = new ThreeReelPayoutCalculator(config);
    }

    @Test
    void testCalculateMultiplier_returns_no_multiplier_when_no_hit_and_no_diamond() {
        ThreeReelSpinDTO spin = new ThreeReelSpinDTO(
                SlotSymbols.CHERRY,
                SlotSymbols.LEMON,
                SlotSymbols.BELL);

        BigDecimal result = calculator.calculateMultiplier(spin);

        assertEquals(0, new BigDecimal("0").compareTo(result));
    }

    @Test
    void testCalculateMultiplier_returns_single_diamond_multiplier_when_one_diamond_exists() {
        ThreeReelSpinDTO spin = new ThreeReelSpinDTO(
                SlotSymbols.DIAMOND,
                SlotSymbols.CHERRY,
                SlotSymbols.LEMON);

        BigDecimal result = calculator.calculateMultiplier(spin);

        assertEquals(0, new BigDecimal("1.50").compareTo(result));
    }

    @Test
    void testCalculateMultiplier_returns_double_cherry_multiplier_when_two_cherries_exist() {
        ThreeReelSpinDTO spin = new ThreeReelSpinDTO(
                SlotSymbols.CHERRY,
                SlotSymbols.CHERRY,
                SlotSymbols.LEMON);

        BigDecimal result = calculator.calculateMultiplier(spin);

        assertEquals(0, new BigDecimal("0.80").compareTo(result));
    }

    @Test
    void testCalculateMultiplier_returns_triple_cherry_multiplier_when_three_cherries_exist() {
        ThreeReelSpinDTO spin = new ThreeReelSpinDTO(
                SlotSymbols.CHERRY,
                SlotSymbols.CHERRY,
                SlotSymbols.CHERRY);

        BigDecimal result = calculator.calculateMultiplier(spin);

        assertEquals(0, new BigDecimal("0.99").compareTo(result));
    }

    @Test
    void testCalculateMultiplier_returns_double_lemon_multiplier_when_two_lemons_exist() {
        ThreeReelSpinDTO spin = new ThreeReelSpinDTO(
                SlotSymbols.LEMON,
                SlotSymbols.LEMON,
                SlotSymbols.CHERRY);

        BigDecimal result = calculator.calculateMultiplier(spin);

        assertEquals(0, new BigDecimal("1.15").compareTo(result));
    }

    @Test
    void testCalculateMultiplier_returns_triple_lemon_multiplier_when_three_lemons_exist() {
        ThreeReelSpinDTO spin = new ThreeReelSpinDTO(
                SlotSymbols.LEMON,
                SlotSymbols.LEMON,
                SlotSymbols.LEMON);

        BigDecimal result = calculator.calculateMultiplier(spin);

        assertEquals(0, new BigDecimal("2.50").compareTo(result));
    }

    @Test
    void testCalculateMultiplier_returns_double_bell_multiplier_when_two_bells_exist() {
        ThreeReelSpinDTO spin = new ThreeReelSpinDTO(
                SlotSymbols.BELL,
                SlotSymbols.BELL,
                SlotSymbols.CHERRY);

        BigDecimal result = calculator.calculateMultiplier(spin);

        assertEquals(0, new BigDecimal("1.70").compareTo(result));
    }

    @Test
    void testCalculateMultiplier_returns_triple_bell_multiplier_when_three_bells_exist() {
        ThreeReelSpinDTO spin = new ThreeReelSpinDTO(
                SlotSymbols.BELL,
                SlotSymbols.BELL,
                SlotSymbols.BELL);

        BigDecimal result = calculator.calculateMultiplier(spin);

        assertEquals(0, new BigDecimal("5.75").compareTo(result));
    }

    @Test
    void testCalculateMultiplier_returns_single_diamond_multiplier_when_double_cherry_and_single_diamond_exist() {
        ThreeReelSpinDTO spin = new ThreeReelSpinDTO(
                SlotSymbols.CHERRY,
                SlotSymbols.CHERRY,
                SlotSymbols.DIAMOND);

        BigDecimal result = calculator.calculateMultiplier(spin);

        assertEquals(0, new BigDecimal("1.50").compareTo(result));
    }

    @Test
    void testCalculateMultiplier_returns_double_bell_multiplier_when_double_bell_and_single_diamond_exist() {
        ThreeReelSpinDTO spin = new ThreeReelSpinDTO(
                SlotSymbols.BELL,
                SlotSymbols.BELL,
                SlotSymbols.DIAMOND);

        BigDecimal result = calculator.calculateMultiplier(spin);

        assertEquals(0, new BigDecimal("1.70").compareTo(result));
    }

    @Test
    void testCalculateMultiplier_returns_double_diamond_multiplier_when_two_diamonds_exist() {
        ThreeReelSpinDTO spin = new ThreeReelSpinDTO(
                SlotSymbols.DIAMOND,
                SlotSymbols.DIAMOND,
                SlotSymbols.CHERRY);

        BigDecimal result = calculator.calculateMultiplier(spin);

        assertEquals(0, new BigDecimal("15.80").compareTo(result));
    }

    @Test
    void testCalculateMultiplier_returns_triple_diamond_multiplier_when_three_diamonds_exist() {
        ThreeReelSpinDTO spin = new ThreeReelSpinDTO(
                SlotSymbols.DIAMOND,
                SlotSymbols.DIAMOND,
                SlotSymbols.DIAMOND);

        BigDecimal result = calculator.calculateMultiplier(spin);

        assertEquals(0, new BigDecimal("498.00").compareTo(result));
    }
}
