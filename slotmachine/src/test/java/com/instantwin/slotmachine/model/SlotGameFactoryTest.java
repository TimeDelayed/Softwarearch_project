package com.instantwin.slotmachine.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.instantwin.slotmachine.dto.ThreeReelSpinDTO;
import com.instantwin.slotmachine.utilities.SlotSymbols;

public class SlotGameFactoryTest {

    @Test
    void testCreateSlotGame_creates_slot_game_with_correct_values() {
        var factory = new SlotGameFactory();
        var spinResult = new ThreeReelSpinDTO(
                SlotSymbols.DIAMOND,
                SlotSymbols.CHERRY,
                SlotSymbols.LEMON);

        var result = factory.createSlotGame(
                1L,
                BigDecimal.TEN,
                true,
                BigDecimal.valueOf(15),
                spinResult);

        assertEquals(1L, result.getUserId());
        assertEquals(BigDecimal.TEN, result.getBetAmount());
        assertTrue(result.isWon());
        assertEquals(BigDecimal.valueOf(15), result.getAmount());
        assertEquals(
                List.of(SlotSymbols.DIAMOND, SlotSymbols.CHERRY, SlotSymbols.LEMON),
                result.getSlotStates());
    }
}
