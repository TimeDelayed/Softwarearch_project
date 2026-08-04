package com.instantwin.slotmachine.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.EnumMap;
import java.util.Map;
import java.util.SplittableRandom;

import org.junit.jupiter.api.Test;

import com.instantwin.slotmachine.contract.model.ISlotProbabilityConfiguration;
import com.instantwin.slotmachine.dto.ThreeReelSpinDTO;
import com.instantwin.slotmachine.utilities.InvalidSlotProbabilities;
import com.instantwin.slotmachine.utilities.SlotSymbols;

public class SlotSpinnerTest {

    @Test
    void testSpin_returns_three_cherries_when_cherry_probability_is_one() {
        SlotSpinner spinner = new SlotSpinner(
                mockConfiguration(validProbabilitiesFor(SlotSymbols.CHERRY)),
                new SplittableRandom(42));

        ThreeReelSpinDTO result = spinner.spin();

        assertEquals(SlotSymbols.CHERRY, result.first());
        assertEquals(SlotSymbols.CHERRY, result.second());
        assertEquals(SlotSymbols.CHERRY, result.third());
    }

    @Test
    void testSpin_returns_three_lemons_when_lemon_probability_is_one() {
        SlotSpinner spinner = new SlotSpinner(
                mockConfiguration(validProbabilitiesFor(SlotSymbols.LEMON)),
                new SplittableRandom(42));

        ThreeReelSpinDTO result = spinner.spin();

        assertEquals(SlotSymbols.LEMON, result.first());
        assertEquals(SlotSymbols.LEMON, result.second());
        assertEquals(SlotSymbols.LEMON, result.third());
    }

    @Test
    void testSpin_returns_three_diamonds_when_diamond_probability_is_one() {
        SlotSpinner spinner = new SlotSpinner(
                mockConfiguration(validProbabilitiesFor(SlotSymbols.DIAMOND)),
                new SplittableRandom(42));

        ThreeReelSpinDTO result = spinner.spin();

        assertEquals(SlotSymbols.DIAMOND, result.first());
        assertEquals(SlotSymbols.DIAMOND, result.second());
        assertEquals(SlotSymbols.DIAMOND, result.third());
    }

    @Test
    void testSpin_returns_three_bells_when_bell_probability_is_one() {
        SlotSpinner spinner = new SlotSpinner(
                mockConfiguration(validProbabilitiesFor(SlotSymbols.BELL)),
                new SplittableRandom(42));

        ThreeReelSpinDTO result = spinner.spin();

        assertEquals(SlotSymbols.BELL, result.first());
        assertEquals(SlotSymbols.BELL, result.second());
        assertEquals(SlotSymbols.BELL, result.third());
    }

    @Test
    void testConstructor_throws_invalid_slot_probabilities_when_symbol_is_missing() {
        assertThrows(
                InvalidSlotProbabilities.class,
                () -> new SlotSpinner(
                        mockConfiguration(Map.of(SlotSymbols.CHERRY, 1.0f)),
                        new SplittableRandom(42)));
    }

    @Test
    void testConstructor_throws_invalid_slot_probabilities_when_probability_is_negative() {
        var probabilities = validProbabilitiesFor(SlotSymbols.CHERRY);
        probabilities.put(SlotSymbols.LEMON, -0.1f);

        assertThrows(
                InvalidSlotProbabilities.class,
                () -> new SlotSpinner(
                        mockConfiguration(probabilities),
                        new SplittableRandom(42)));
    }

    @Test
    void testConstructor_throws_invalid_slot_probabilities_when_probability_is_greater_than_one() {
        var probabilities = validProbabilitiesFor(SlotSymbols.CHERRY);
        probabilities.put(SlotSymbols.CHERRY, 1.1f);

        assertThrows(
                InvalidSlotProbabilities.class,
                () -> new SlotSpinner(
                        mockConfiguration(probabilities),
                        new SplittableRandom(42)));
    }

    @Test
    void testConstructor_throws_invalid_slot_probabilities_when_total_probability_is_too_low() {
        var probabilities = validProbabilitiesFor(SlotSymbols.CHERRY);
        probabilities.put(SlotSymbols.CHERRY, 0.9f);

        assertThrows(
                InvalidSlotProbabilities.class,
                () -> new SlotSpinner(
                        mockConfiguration(probabilities),
                        new SplittableRandom(42)));
    }

    @Test
    void testConstructor_throws_invalid_slot_probabilities_when_total_probability_is_too_high() {
        var probabilities = validProbabilitiesFor(SlotSymbols.CHERRY);
        probabilities.put(SlotSymbols.LEMON, 0.1f);

        assertThrows(
                InvalidSlotProbabilities.class,
                () -> new SlotSpinner(
                        mockConfiguration(probabilities),
                        new SplittableRandom(42)));
    }

    private ISlotProbabilityConfiguration mockConfiguration(Map<SlotSymbols, Float> probabilities) {
        ISlotProbabilityConfiguration config = mock(ISlotProbabilityConfiguration.class);
        when(config.getProbabilities()).thenReturn(probabilities);
        return config;
    }

    private EnumMap<SlotSymbols, Float> validProbabilitiesFor(SlotSymbols selectedSymbol) {
        var probabilities = new EnumMap<SlotSymbols, Float>(SlotSymbols.class);

        for (var symbol : SlotSymbols.values()) {
            probabilities.put(symbol, 0.0f);
        }

        probabilities.put(selectedSymbol, 1.0f);
        return probabilities;
    }
}
