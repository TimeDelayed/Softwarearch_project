package com.instantwin.slotmachine.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.instantwin.slotmachine.contract.service.ISlotGameService;
import com.instantwin.slotmachine.dto.SlotGameRequestBodyDTO;
import com.instantwin.slotmachine.utilities.SlotSymbols;
import com.instantwin.slotmachine.view.SlotClientStatsView;
import com.instantwin.slotmachine.view.SlotGameView;
import com.instantwin.slotmachine.view.SlotHouseStatsView;

public class SlotControllerTest {

    private static final long USER_ID = 1L;
    private static final long GAME_ID = 10L;
    private static final BigDecimal BET_AMOUNT = BigDecimal.TEN;

    private ISlotGameService slotGameService;
    private SlotController slotController;

    @BeforeEach
    void setUp() {
        slotGameService = mock(ISlotGameService.class);
        slotController = new SlotController(slotGameService);
    }

    @Test
    void testGetGameRules_returns_game_rules_with_ok_status() {
        when(slotGameService.getGameRules()).thenReturn("GAME RULES");

        var result = slotController.getGameRules();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("GAME RULES", result.getBody());
    }

    @Test
    void testGetSlotChances_returns_slot_chances_as_plain_text() {
        when(slotGameService.getGameChances()).thenReturn("GAME INFORMATION");

        var result = slotController.getSlotChances();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(MediaType.TEXT_PLAIN, result.getHeaders().getContentType());
        assertEquals("GAME INFORMATION", result.getBody());
    }

    @Test
    void testGetSlotHouseStats_returns_house_statistics() {
        var houseStats = new SlotHouseStatsView(
                2,
                5,
                BigDecimal.valueOf(20),
                BigDecimal.valueOf(30),
                BigDecimal.valueOf(50));
        when(slotGameService.getHouseStats()).thenReturn(houseStats);

        var result = slotController.getSlotHouseStats();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(houseStats, result.getBody());
    }

    @Test
    void testGetSlotUserStats_returns_user_statistics() {
        var userStats = new SlotClientStatsView(
                USER_ID,
                2,
                BigDecimal.valueOf(15),
                BigDecimal.TEN,
                BigDecimal.valueOf(5),
                BigDecimal.valueOf(20),
                BigDecimal.valueOf(-5));
        when(slotGameService.getUserStats(USER_ID)).thenReturn(Optional.of(userStats));

        var result = slotController.getSlotUserStats(USER_ID);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(userStats, result.getBody());
    }

    @Test
    void testGetSlotUserStats_returns_not_found_when_user_has_no_slot_games() {
        when(slotGameService.getUserStats(USER_ID)).thenReturn(Optional.empty());

        var result = slotController.getSlotUserStats(USER_ID);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    void testGetAllGames_returns_all_slot_games() {
        var game = slotGameView();
        when(slotGameService.findAll()).thenReturn(List.of(game));

        var result = slotController.getAllGames();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(List.of(game), result.getBody());
    }

    @Test
    void testGetGameStats_returns_slot_game_when_slot_game_exists() {
        var game = slotGameView();
        when(slotGameService.findById(GAME_ID)).thenReturn(Optional.of(game));

        var result = slotController.getGameStats(GAME_ID);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(game, result.getBody());
    }

    @Test
    void testGetGameStats_returns_not_found_when_slot_game_does_not_exist() {
        when(slotGameService.findById(GAME_ID)).thenReturn(Optional.empty());

        var result = slotController.getGameStats(GAME_ID);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    void testPlaySlotGame_delegates_request_to_service() {
        var request = new SlotGameRequestBodyDTO(USER_ID, BET_AMOUNT);
        when(slotGameService.playSlotGame(USER_ID, BET_AMOUNT))
                .thenReturn(Optional.of(slotGameView()));

        slotController.playSlotGame(request);

        verify(slotGameService).playSlotGame(USER_ID, BET_AMOUNT);
    }

    @Test
    void testPlaySlotGame_returns_slot_game_when_transaction_succeeds() {
        var request = new SlotGameRequestBodyDTO(USER_ID, BET_AMOUNT);
        var game = slotGameView();
        when(slotGameService.playSlotGame(USER_ID, BET_AMOUNT)).thenReturn(Optional.of(game));

        var result = slotController.playSlotGame(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(game, result.getBody());
    }

    @Test
    void testPlaySlotGame_returns_not_found_when_transaction_is_rejected() {
        var request = new SlotGameRequestBodyDTO(USER_ID, BET_AMOUNT);
        when(slotGameService.playSlotGame(USER_ID, BET_AMOUNT)).thenReturn(Optional.empty());

        var result = slotController.playSlotGame(request);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    void testDeleteGameStats_returns_deleted_slot_game() {
        var game = slotGameView();
        when(slotGameService.deleteSlotGame(GAME_ID)).thenReturn(Optional.of(game));

        var result = slotController.deleteGameStats(GAME_ID);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(game, result.getBody());
    }

    @Test
    void testDeleteGameStats_returns_not_found_when_slot_game_does_not_exist() {
        when(slotGameService.deleteSlotGame(GAME_ID)).thenReturn(Optional.empty());

        var result = slotController.deleteGameStats(GAME_ID);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertNull(result.getBody());
    }

    private SlotGameView slotGameView() {
        return new SlotGameView(
                GAME_ID,
                USER_ID,
                BET_AMOUNT,
                true,
                BigDecimal.valueOf(15),
                List.of(SlotSymbols.DIAMOND, SlotSymbols.CHERRY, SlotSymbols.LEMON));
    }
}
