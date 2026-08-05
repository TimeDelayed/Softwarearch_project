package com.instantwin.slotmachine.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.instantwin.slotmachine.utilities.SlotBetMultipliers;
import com.instantwin.slotmachine.utilities.SlotSymbols;

public class SlotConfigurationTest {

    private SlotConfiguration slotConfiguration;

    @BeforeEach
    void setUp() {
        slotConfiguration = new SlotConfiguration();
    }

    @Test
    void testGetProbabilities_returns_probability_for_every_symbol() {
        var probabilities = slotConfiguration.getProbabilities();

        assertEquals(4, probabilities.size());
        assertEquals(0.48325f, probabilities.get(SlotSymbols.CHERRY));
        assertEquals(0.29f, probabilities.get(SlotSymbols.LEMON));
        assertEquals(0.21f, probabilities.get(SlotSymbols.BELL));
        assertEquals(0.01675f, probabilities.get(SlotSymbols.DIAMOND));
    }

    @Test
    void testGetProbabilities_returns_probabilities_with_total_of_one() {
        double totalProbability = slotConfiguration.getProbabilities().values().stream()
                .mapToDouble(Float::doubleValue)
                .sum();

        assertEquals(1.0, totalProbability, 0.00001);
    }

    @Test
    void testGetDoubleHitReferenceMap_returns_correct_multipliers() {
        var result = slotConfiguration.getDoubleHitReferenceMap();

        assertEquals(SlotBetMultipliers.DOUBLE_CHERRY, result.get(SlotSymbols.CHERRY));
        assertEquals(SlotBetMultipliers.DOUBLE_LEMON, result.get(SlotSymbols.LEMON));
        assertEquals(SlotBetMultipliers.DOUBLE_BELL, result.get(SlotSymbols.BELL));
        assertEquals(SlotBetMultipliers.DOUBLE_DIAMOND, result.get(SlotSymbols.DIAMOND));
    }

    @Test
    void testGetTripleHitReferenceMap_returns_correct_multipliers() {
        var result = slotConfiguration.getTripleHitReferenceMap();

        assertEquals(SlotBetMultipliers.TRIPLE_CHERRY, result.get(SlotSymbols.CHERRY));
        assertEquals(SlotBetMultipliers.TRIPLE_LEMON, result.get(SlotSymbols.LEMON));
        assertEquals(SlotBetMultipliers.TRIPLE_BELL, result.get(SlotSymbols.BELL));
        assertEquals(SlotBetMultipliers.TRIPLE_DIAMOND, result.get(SlotSymbols.DIAMOND));
    }

    @Test
    void testGetSymbolMultipliers_returns_configured_payout_multipliers() {
        var result = slotConfiguration.getSymbolMultipliers();

        assertEquals(new BigDecimal("0"), result.get(SlotBetMultipliers.NO_MULTIPLIER));
        assertEquals(new BigDecimal("0.80"), result.get(SlotBetMultipliers.DOUBLE_CHERRY));
        assertEquals(new BigDecimal("0.99"), result.get(SlotBetMultipliers.TRIPLE_CHERRY));
        assertEquals(new BigDecimal("1.15"), result.get(SlotBetMultipliers.DOUBLE_LEMON));
        assertEquals(new BigDecimal("2.50"), result.get(SlotBetMultipliers.TRIPLE_LEMON));
        assertEquals(new BigDecimal("1.70"), result.get(SlotBetMultipliers.DOUBLE_BELL));
        assertEquals(new BigDecimal("5.75"), result.get(SlotBetMultipliers.TRIPLE_BELL));
        assertEquals(new BigDecimal("1.50"), result.get(SlotBetMultipliers.SINGLE_DIAMOND));
        assertEquals(new BigDecimal("15.80"), result.get(SlotBetMultipliers.DOUBLE_DIAMOND));
        assertEquals(new BigDecimal("498.00"), result.get(SlotBetMultipliers.TRIPLE_DIAMOND));
    }

    @Test
    void testGetNumberOfReels_returns_three_reels() {
        assertEquals(3, slotConfiguration.getNumberOfReels());
    }
}
