package com.instantwin.slotmachine.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.SplittableRandom;

import org.junit.jupiter.api.Test;

import com.instantwin.slotmachine.contract.model.ISlotConfiguration;
import com.instantwin.slotmachine.dto.ThreeReelSpinDTO;
import com.instantwin.slotmachine.utilities.InvalidSlotProbabilities;
import com.instantwin.slotmachine.utilities.SlotSymbols;

class SlotSpinnerTest {

    @Test
    void testSpin_returns_three_cherries_when_cherry_probability_is_one() {
        SlotSpinner spinner = new SlotSpinner(
                mockConfiguration(Map.of(SlotSymbols.CHERRY, 1.0f)),
                new SplittableRandom(42));

        ThreeReelSpinDTO result = spinner.spin();

        assertEquals(SlotSymbols.CHERRY, result.first());
        assertEquals(SlotSymbols.CHERRY, result.second());
        assertEquals(SlotSymbols.CHERRY, result.third());
    }

    @Test
    void testSpin_returns_three_lemons_when_lemon_probability_is_one() {
        SlotSpinner spinner = new SlotSpinner(
                mockConfiguration(Map.of(SlotSymbols.LEMON, 1.0f)),
                new SplittableRandom(42));

        ThreeReelSpinDTO result = spinner.spin();

        assertEquals(SlotSymbols.LEMON, result.first());
        assertEquals(SlotSymbols.LEMON, result.second());
        assertEquals(SlotSymbols.LEMON, result.third());
    }

    @Test
    void testSpin_returns_three_diamonds_when_diamond_probability_is_one() {
        SlotSpinner spinner = new SlotSpinner(
                mockConfiguration(Map.of(SlotSymbols.DIAMOND, 1.0f)),
                new SplittableRandom(42));

        ThreeReelSpinDTO result = spinner.spin();

        assertEquals(SlotSymbols.DIAMOND, result.first());
        assertEquals(SlotSymbols.DIAMOND, result.second());
        assertEquals(SlotSymbols.DIAMOND, result.third());
    }

    @Test
    void testSpin_returns_three_bells_when_bell_probability_is_one() {
        SlotSpinner spinner = new SlotSpinner(
                mockConfiguration(Map.of(SlotSymbols.BELL, 1.0f)),
                new SplittableRandom(42));

        ThreeReelSpinDTO result = spinner.spin();

        assertEquals(SlotSymbols.BELL, result.first());
        assertEquals(SlotSymbols.BELL, result.second());
        assertEquals(SlotSymbols.BELL, result.third());
    }

    @Test
    void testSpin_throws_invalid_slot_probabilities_when_total_probability_is_too_low() {
        SlotSpinner spinner = new SlotSpinner(
                mockConfiguration(Map.of(SlotSymbols.CHERRY, 0.0f)),
                new SplittableRandom(42));

        assertThrows(InvalidSlotProbabilities.class, spinner::spin);
    }

    private ISlotConfiguration mockConfiguration(Map<SlotSymbols, Float> probabilities) {
        ISlotConfiguration config = mock(ISlotConfiguration.class);
        when(config.getProbabilities()).thenReturn(probabilities);
        return config;
    }
}
