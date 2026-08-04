package com.instantwin.slotmachine.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.instantwin.slotmachine.contract.model.ISlotSpinner;
import com.instantwin.slotmachine.contract.model.IThreeReelPayoutCalculator;
import com.instantwin.slotmachine.dto.ThreeReelSpinDTO;
import com.instantwin.slotmachine.utilities.BetAmountInvalidException;
import com.instantwin.slotmachine.utilities.SlotErrorMessages;
import com.instantwin.slotmachine.utilities.SlotSymbols;

public class SlotGameLogicTest {

    private static final BigDecimal BET_AMOUNT = BigDecimal.TEN;

    private static final ThreeReelSpinDTO SPIN_RESULT = new ThreeReelSpinDTO(
            SlotSymbols.DIAMOND,
            SlotSymbols.CHERRY,
            SlotSymbols.LEMON);

    private ISlotSpinner slotSpinner;
    private IThreeReelPayoutCalculator payoutCalculator;
    private SlotGameLogic slotGameLogic;

    @BeforeEach
    void setUp() {
        slotSpinner = mock(ISlotSpinner.class);
        payoutCalculator = mock(IThreeReelPayoutCalculator.class);
        slotGameLogic = new SlotGameLogic(slotSpinner, payoutCalculator);
    }

    @Test
    void testPlaceBet_returns_winning_game_result() {
        when(slotSpinner.spin()).thenReturn(SPIN_RESULT);
        when(payoutCalculator.calculateMultiplier(SPIN_RESULT)).thenReturn(BigDecimal.valueOf(2.5));

        var result = slotGameLogic.placeBet(BET_AMOUNT);

        assertEquals(BET_AMOUNT, result.betAmount());
        assertEquals(SPIN_RESULT, result.spinResultSymbols());
        assertTrue(result.won());
        assertEquals(BigDecimal.valueOf(25.0), result.winnings());
        verify(slotSpinner).spin();
        verify(payoutCalculator).calculateMultiplier(SPIN_RESULT);
    }

    @Test
    void testPlaceBet_returns_losing_game_result_when_multiplier_is_zero() {
        when(slotSpinner.spin()).thenReturn(SPIN_RESULT);
        when(payoutCalculator.calculateMultiplier(SPIN_RESULT)).thenReturn(BigDecimal.ZERO);

        var result = slotGameLogic.placeBet(BET_AMOUNT);

        assertFalse(result.won());
        assertEquals(BigDecimal.ZERO, result.winnings());
    }

    @Test
    void testPlaceBet_throws_exception_when_bet_amount_is_null() {
        var exception = assertThrows(
                BetAmountInvalidException.class,
                () -> slotGameLogic.placeBet(null));

        assertEquals(SlotErrorMessages.BET_AMOUNT_NULL, exception.getMessage());
    }

    @Test
    void testPlaceBet_throws_exception_when_bet_amount_is_zero() {
        var exception = assertThrows(
                BetAmountInvalidException.class,
                () -> slotGameLogic.placeBet(BigDecimal.ZERO));

        assertEquals(SlotErrorMessages.BET_AMOUNT_NEGATIVE, exception.getMessage());
    }

    @Test
    void testPlaceBet_throws_exception_when_bet_amount_is_negative() {
        var exception = assertThrows(
                BetAmountInvalidException.class,
                () -> slotGameLogic.placeBet(BigDecimal.valueOf(-1)));

        assertEquals(SlotErrorMessages.BET_AMOUNT_NEGATIVE, exception.getMessage());
    }
}
