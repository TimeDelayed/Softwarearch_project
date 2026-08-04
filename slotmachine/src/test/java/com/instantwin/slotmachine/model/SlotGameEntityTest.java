package com.instantwin.slotmachine.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.instantwin.slotmachine.dto.ThreeReelSpinDTO;
import com.instantwin.slotmachine.utilities.ModelValidityBreachException;
import com.instantwin.slotmachine.utilities.SlotErrorMessages;
import com.instantwin.slotmachine.utilities.SlotSymbols;

public class SlotGameEntityTest {

    private static final long USER_ID = 1L;
    private static final long NEGATIVE_USER_ID = -1L;

    private static final BigDecimal BET_AMOUNT = BigDecimal.TEN;
    private static final BigDecimal NET_AMOUNT = BigDecimal.valueOf(15);
    private static final BigDecimal NEGATIVE_BET_AMOUNT = BigDecimal.valueOf(-1);

    private static final ThreeReelSpinDTO SPIN_RESULT = new ThreeReelSpinDTO(
            SlotSymbols.DIAMOND,
            SlotSymbols.CHERRY,
            SlotSymbols.LEMON);

    private SlotGameEntity slotGame;

    @BeforeEach
    void setUp() {
        slotGame = SlotGameEntity.of(
                USER_ID,
                BET_AMOUNT,
                true,
                NET_AMOUNT,
                SPIN_RESULT);
    }

    @Test
    void testOf_creates_slot_game_entity_with_valid_input() {
        assertEquals(USER_ID, slotGame.getUserId());
        assertEquals(BET_AMOUNT, slotGame.getBetAmount());
        assertEquals(true, slotGame.isWon());
        assertEquals(NET_AMOUNT, slotGame.getAmount());
        assertEquals(
                List.of(SlotSymbols.DIAMOND, SlotSymbols.CHERRY, SlotSymbols.LEMON),
                slotGame.getSlotStates());
    }

    @Test
    void testOf_throws_exception_when_user_id_is_null() {
        var exception = assertThrows(
                ModelValidityBreachException.class,
                () -> SlotGameEntity.of(null, BET_AMOUNT, true, NET_AMOUNT, SPIN_RESULT));

        assertEquals(SlotErrorMessages.USER_ID_NULL, exception.getMessage());
    }

    @Test
    void testOf_throws_exception_when_user_id_is_negative() {
        var exception = assertThrows(
                ModelValidityBreachException.class,
                () -> SlotGameEntity.of(NEGATIVE_USER_ID, BET_AMOUNT, true, NET_AMOUNT, SPIN_RESULT));

        assertEquals(SlotErrorMessages.USER_ID_NEGATIVE, exception.getMessage());
    }

    @Test
    void testOf_throws_exception_when_user_id_is_zero() {
        var exception = assertThrows(
                ModelValidityBreachException.class,
                () -> SlotGameEntity.of(0L, BET_AMOUNT, true, NET_AMOUNT, SPIN_RESULT));

        assertEquals(SlotErrorMessages.USER_ID_NEGATIVE, exception.getMessage());
    }

    @Test
    void testOf_throws_exception_when_bet_amount_is_null() {
        var exception = assertThrows(
                ModelValidityBreachException.class,
                () -> SlotGameEntity.of(USER_ID, null, true, NET_AMOUNT, SPIN_RESULT));

        assertEquals(SlotErrorMessages.INVALID_AMOUNT_NULL, exception.getMessage());
    }

    @Test
    void testOf_throws_exception_when_net_amount_is_null() {
        var exception = assertThrows(
                ModelValidityBreachException.class,
                () -> SlotGameEntity.of(USER_ID, BET_AMOUNT, true, null, SPIN_RESULT));

        assertEquals(SlotErrorMessages.INVALID_AMOUNT_NULL, exception.getMessage());
    }

    @Test
    void testOf_throws_exception_when_bet_amount_is_negative() {
        var exception = assertThrows(
                ModelValidityBreachException.class,
                () -> SlotGameEntity.of(USER_ID, NEGATIVE_BET_AMOUNT, false, NET_AMOUNT, SPIN_RESULT));

        assertEquals(SlotErrorMessages.INVALID_AMOUNT_NEGATIVE, exception.getMessage());
    }

    @Test
    void testOf_throws_exception_when_bet_amount_is_zero() {
        var exception = assertThrows(
                ModelValidityBreachException.class,
                () -> SlotGameEntity.of(USER_ID, BigDecimal.ZERO, false, BigDecimal.ZERO, SPIN_RESULT));

        assertEquals(SlotErrorMessages.INVALID_AMOUNT_NEGATIVE, exception.getMessage());
    }

    @Test
    void testOf_throws_exception_when_player_loses_more_than_bet_amount() {
        var exception = assertThrows(
                ModelValidityBreachException.class,
                () -> SlotGameEntity.of(USER_ID, BET_AMOUNT, false, BigDecimal.valueOf(-11), SPIN_RESULT));

        assertEquals(SlotErrorMessages.INVALID_NET_AMOUNT, exception.getMessage());
    }

    @Test
    void testOf_throws_exception_when_win_state_does_not_match_cash_out() {
        var exception = assertThrows(
                ModelValidityBreachException.class,
                () -> SlotGameEntity.of(USER_ID, BET_AMOUNT, true, BET_AMOUNT.negate(), SPIN_RESULT));

        assertEquals(SlotErrorMessages.INVALID_WIN_STATE, exception.getMessage());
    }

    @Test
    void testOf_throws_exception_when_spin_result_is_null() {
        var exception = assertThrows(
                ModelValidityBreachException.class,
                () -> SlotGameEntity.of(USER_ID, BET_AMOUNT, true, NET_AMOUNT, null));

        assertEquals(SlotErrorMessages.INVALID_SLOT_STATES_NULL, exception.getMessage());
    }

    @Test
    void testOf_throws_exception_when_spin_result_contains_null_symbol() {
        var invalidSpinResult = new ThreeReelSpinDTO(
                SlotSymbols.DIAMOND,
                null,
                SlotSymbols.LEMON);

        var exception = assertThrows(
                ModelValidityBreachException.class,
                () -> SlotGameEntity.of(USER_ID, BET_AMOUNT, true, NET_AMOUNT, invalidSpinResult));

        assertEquals(SlotErrorMessages.INVALID_SLOT_STATES_NULL, exception.getMessage());
    }

    @Test
    void testGetSlotStates_returns_unmodifiable_list() {
        var slotStates = slotGame.getSlotStates();

        assertThrows(UnsupportedOperationException.class,
                () -> slotStates.set(0, SlotSymbols.BELL));
    }
}
