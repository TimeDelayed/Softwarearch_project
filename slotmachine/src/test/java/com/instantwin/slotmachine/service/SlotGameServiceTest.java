package com.instantwin.slotmachine.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import com.instantwin.slotmachine.contract.client.ISlotRequestTransactionClient;
import com.instantwin.slotmachine.contract.model.ISlotGameFactory;
import com.instantwin.slotmachine.contract.model.ISlotGameLogic;
import com.instantwin.slotmachine.dto.ThreeReelSpinDTO;
import com.instantwin.slotmachine.model.SlotGameEntity;
import com.instantwin.slotmachine.repository.ISlotRepository;
import com.instantwin.slotmachine.utilities.SlotSymbols;
import com.instantwin.slotmachine.view.SlotGameResultView;

public class SlotGameServiceTest {

    private static final long USER_ID = 1L;
    private static final long OTHER_USER_ID = 2L;
    private static final long GAME_ID = 10L;
    private static final long UNKNOWN_GAME_ID = 999L;

    private static final BigDecimal BET_AMOUNT = BigDecimal.valueOf(10);
    private static final BigDecimal OTHER_BET_AMOUNT = BigDecimal.valueOf(20);
    private static final BigDecimal WINNINGS = BigDecimal.valueOf(25);
    private static final BigDecimal PLAYER_PROFIT = BigDecimal.valueOf(15);
    private static final BigDecimal PLAYER_LOSS = BigDecimal.valueOf(-10);

    private static final ThreeReelSpinDTO WINNING_SPIN = new ThreeReelSpinDTO(
            SlotSymbols.DIAMOND,
            SlotSymbols.CHERRY,
            SlotSymbols.LEMON);

    private static final ThreeReelSpinDTO LOSING_SPIN = new ThreeReelSpinDTO(
            SlotSymbols.CHERRY,
            SlotSymbols.LEMON,
            SlotSymbols.BELL);

    private ISlotGameLogic slotGameLogic;
    private ISlotRepository slotGameRepository;
    private ISlotRequestTransactionClient slotRequestTransactionClient;
    private ISlotGameFactory slotGameFactory;
    private SlotGameService slotGameService;

    @BeforeEach
    void setUpService() {
        slotGameLogic = mock(ISlotGameLogic.class);
        slotGameRepository = mock(ISlotRepository.class);
        slotRequestTransactionClient = mock(ISlotRequestTransactionClient.class);
        slotGameFactory = mock(ISlotGameFactory.class);
        slotGameService = new SlotGameService(
                slotGameLogic,
                slotGameRepository,
                slotRequestTransactionClient,
                slotGameFactory);
    }

    @Test
    void testFindAll_returns_all_slot_games() {
        var firstGame = slotGame(
                GAME_ID,
                USER_ID,
                BET_AMOUNT,
                true,
                PLAYER_PROFIT,
                WINNING_SPIN);
        var secondGame = slotGame(
                GAME_ID + 1,
                OTHER_USER_ID,
                OTHER_BET_AMOUNT,
                false,
                OTHER_BET_AMOUNT.negate(),
                LOSING_SPIN);
        when(slotGameRepository.findAll()).thenReturn(List.of(firstGame, secondGame));

        var result = slotGameService.findAll();

        assertEquals(2, result.size());
        assertEquals(GAME_ID, result.get(0).getId());
        assertEquals(USER_ID, result.get(0).getUserId());
        assertEquals(BET_AMOUNT, result.get(0).getBetAmount());
        assertTrue(result.get(0).getWon());
        assertEquals(PLAYER_PROFIT, result.get(0).getAmount());
        assertEquals(slotStates(WINNING_SPIN), result.get(0).getSlotStates());

        assertEquals(GAME_ID + 1, result.get(1).getId());
        assertEquals(OTHER_USER_ID, result.get(1).getUserId());
        assertEquals(OTHER_BET_AMOUNT, result.get(1).getBetAmount());
        assertFalse(result.get(1).getWon());
        assertEquals(OTHER_BET_AMOUNT.negate(), result.get(1).getAmount());
    }

    @Test
    void testFindAll_returns_empty_list_when_no_slot_games_exist() {
        when(slotGameRepository.findAll()).thenReturn(List.of());

        var result = slotGameService.findAll();

        assertTrue(result.isEmpty());
    }

    @Test
    void testFindAllByUserId_returns_all_slot_games_of_user() {
        var firstGame = slotGame(
                GAME_ID,
                USER_ID,
                BET_AMOUNT,
                true,
                PLAYER_PROFIT,
                WINNING_SPIN);
        var secondGame = slotGame(
                GAME_ID + 1,
                USER_ID,
                BET_AMOUNT,
                false,
                PLAYER_LOSS,
                LOSING_SPIN);
        when(slotGameRepository.findAllByUserId(USER_ID)).thenReturn(List.of(firstGame, secondGame));

        var result = slotGameService.findAllByUserId(USER_ID);

        assertEquals(2, result.size());
        assertEquals(USER_ID, result.get(0).getUserId());
        assertEquals(PLAYER_PROFIT, result.get(0).getAmount());
        assertEquals(USER_ID, result.get(1).getUserId());
        assertEquals(PLAYER_LOSS, result.get(1).getAmount());
    }

    @Test
    void testFindAllByUserId_returns_empty_list_when_user_has_no_slot_games() {
        when(slotGameRepository.findAllByUserId(USER_ID)).thenReturn(List.of());

        var result = slotGameService.findAllByUserId(USER_ID);

        assertTrue(result.isEmpty());
    }

    @Test
    void testPlaySlotGame_requests_transaction_with_player_net_result() {
        successfulSlotGameRequest();

        slotGameService.playSlotGame(USER_ID, BET_AMOUNT);

        verify(slotRequestTransactionClient).requestTransaction(USER_ID, PLAYER_PROFIT);
    }

    @Test
    void testPlaySlotGame_returns_empty_optional_when_transaction_is_rejected() {
        when(slotGameLogic.placeBet(BET_AMOUNT)).thenReturn(winningGameResult());
        when(slotRequestTransactionClient.requestTransaction(USER_ID, PLAYER_PROFIT))
                .thenReturn(ResponseEntity.status(404).body("User not found"));

        var result = slotGameService.playSlotGame(USER_ID, BET_AMOUNT);

        assertTrue(result.isEmpty());
        verify(slotGameFactory, never()).createSlotGame(
                anyLong(),
                any(BigDecimal.class),
                anyBoolean(),
                any(BigDecimal.class),
                any(ThreeReelSpinDTO.class));
        verify(slotGameRepository, never()).save(any());
    }

    @Test
    void testPlaySlotGame_creates_and_saves_slot_game_when_transaction_succeeds() {
        var entity = successfulSlotGameRequest();

        var result = slotGameService.playSlotGame(USER_ID, BET_AMOUNT);

        assertTrue(result.isPresent());
        verify(slotGameFactory).createSlotGame(
                USER_ID,
                BET_AMOUNT,
                true,
                PLAYER_PROFIT,
                WINNING_SPIN);
        verify(slotGameRepository).save(entity);
    }

    @Test
    void testPlaySlotGame_returns_created_slot_game() {
        successfulSlotGameRequest();

        var result = slotGameService.playSlotGame(USER_ID, BET_AMOUNT);

        assertTrue(result.isPresent());
        assertEquals(GAME_ID, result.get().getId());
        assertEquals(USER_ID, result.get().getUserId());
        assertEquals(BET_AMOUNT, result.get().getBetAmount());
        assertTrue(result.get().getWon());
        assertEquals(PLAYER_PROFIT, result.get().getAmount());
        assertEquals(slotStates(WINNING_SPIN), result.get().getSlotStates());
    }

    @Test
    void testFindById_returns_slot_game_when_slot_game_exists() {
        var entity = slotGame(
                GAME_ID,
                USER_ID,
                BET_AMOUNT,
                true,
                PLAYER_PROFIT,
                WINNING_SPIN);
        when(slotGameRepository.findById(GAME_ID)).thenReturn(Optional.of(entity));

        var result = slotGameService.findById(GAME_ID);

        assertTrue(result.isPresent());
        assertEquals(GAME_ID, result.get().getId());
        assertEquals(USER_ID, result.get().getUserId());
        assertEquals(BET_AMOUNT, result.get().getBetAmount());
        assertEquals(PLAYER_PROFIT, result.get().getAmount());
    }

    @Test
    void testFindById_returns_empty_optional_when_slot_game_does_not_exist() {
        when(slotGameRepository.findById(UNKNOWN_GAME_ID)).thenReturn(Optional.empty());

        var result = slotGameService.findById(UNKNOWN_GAME_ID);

        assertTrue(result.isEmpty());
    }

    @Test
    void testDeleteSlotGame_returns_empty_optional_when_slot_game_does_not_exist() {
        when(slotGameRepository.findById(UNKNOWN_GAME_ID)).thenReturn(Optional.empty());

        var result = slotGameService.deleteSlotGame(UNKNOWN_GAME_ID);

        assertTrue(result.isEmpty());
        verify(slotGameRepository, never()).delete(any());
    }

    @Test
    void testDeleteSlotGame_deletes_slot_game_from_repository() {
        var entity = slotGame(
                GAME_ID,
                USER_ID,
                BET_AMOUNT,
                false,
                PLAYER_LOSS,
                LOSING_SPIN);
        when(slotGameRepository.findById(GAME_ID)).thenReturn(Optional.of(entity));

        var result = slotGameService.deleteSlotGame(GAME_ID);

        assertTrue(result.isPresent());
        verify(slotGameRepository).delete(entity);
    }

    @Test
    void testDeleteSlotGame_returns_deleted_slot_game() {
        var entity = slotGame(
                GAME_ID,
                USER_ID,
                BET_AMOUNT,
                false,
                PLAYER_LOSS,
                LOSING_SPIN);
        when(slotGameRepository.findById(GAME_ID)).thenReturn(Optional.of(entity));

        var result = slotGameService.deleteSlotGame(GAME_ID);

        assertTrue(result.isPresent());
        assertEquals(GAME_ID, result.get().getId());
        assertEquals(USER_ID, result.get().getUserId());
        assertEquals(BET_AMOUNT, result.get().getBetAmount());
        assertFalse(result.get().getWon());
        assertEquals(PLAYER_LOSS, result.get().getAmount());
    }

    @Test
    void testGetGameRules_returns_game_rules() {
        var result = slotGameService.getGameRules();

        assertTrue(result.contains("GAME RULES"));
        assertTrue(result.contains("Paytable"));
    }

    @Test
    void testGetGameChances_returns_game_chances() {
        var result = slotGameService.getGameChances();

        assertTrue(result.contains("GAME INFORMATION"));
        assertTrue(result.contains("Approximate RTP"));
    }

    @Test
    void testGetHouseStats_returns_zero_values_when_no_slot_games_exist() {
        when(slotGameRepository.findAll()).thenReturn(List.of());

        var result = slotGameService.getHouseStats();

        assertEquals(0, result.getTotalClientCount());
        assertEquals(0, result.getTotalGamesCount());
        assertEquals(BigDecimal.ZERO, result.getTotalProfit());
        assertEquals(BigDecimal.ZERO, result.getTotalCashout());
        assertEquals(BigDecimal.ZERO, result.getTotalTurnover());
    }

    @Test
    void testGetHouseStats_calculates_house_statistics() {
        var firstGame = slotGame(
                GAME_ID,
                USER_ID,
                BET_AMOUNT,
                false,
                BigDecimal.valueOf(-10),
                LOSING_SPIN);
        var secondGame = slotGame(
                GAME_ID + 1,
                USER_ID,
                BET_AMOUNT,
                true,
                BigDecimal.valueOf(5),
                WINNING_SPIN);
        var thirdGame = slotGame(
                GAME_ID + 2,
                OTHER_USER_ID,
                OTHER_BET_AMOUNT,
                false,
                BigDecimal.valueOf(-5),
                LOSING_SPIN);
        when(slotGameRepository.findAll()).thenReturn(List.of(firstGame, secondGame, thirdGame));

        var result = slotGameService.getHouseStats();

        assertEquals(2, result.getTotalClientCount());
        assertEquals(3, result.getTotalGamesCount());
        assertEquals(BigDecimal.valueOf(10), result.getTotalProfit());
        assertEquals(BigDecimal.valueOf(30), result.getTotalCashout());
        assertEquals(BigDecimal.valueOf(40), result.getTotalTurnover());
    }

    @Test
    void testGetUserStats_returns_empty_when_user_has_no_slot_games() {
        when(slotGameRepository.findAllByUserId(USER_ID)).thenReturn(List.of());

        var result = slotGameService.getUserStats(USER_ID);

        assertTrue(result.isEmpty());
    }

    @Test
    void testGetUserStats_calculates_user_statistics() {
        var winningGame = slotGame(
                GAME_ID,
                USER_ID,
                BET_AMOUNT,
                true,
                PLAYER_PROFIT,
                WINNING_SPIN);
        var losingGame = slotGame(
                GAME_ID + 1,
                USER_ID,
                BET_AMOUNT,
                false,
                PLAYER_LOSS,
                LOSING_SPIN);
        when(slotGameRepository.findAllByUserId(USER_ID)).thenReturn(List.of(winningGame, losingGame));

        var result = slotGameService.getUserStats(USER_ID);

        assertTrue(result.isPresent());
        assertEquals(USER_ID, result.get().getUserId());
        assertEquals(2, result.get().getTotalGamesCount());
        assertEquals(PLAYER_PROFIT, result.get().getTotalWinnings());
        assertEquals(BET_AMOUNT, result.get().getTotalLosses());
        assertEquals(BigDecimal.valueOf(5), result.get().getTotalClientProfit());
        assertEquals(BigDecimal.valueOf(20), result.get().getTotalHouseTurnoverFromClient());
        assertEquals(BigDecimal.valueOf(-5), result.get().getTotalHouseProfitFromClient());
    }

    private SlotGameEntity successfulSlotGameRequest() {
        var entity = slotGame(
                GAME_ID,
                USER_ID,
                BET_AMOUNT,
                true,
                PLAYER_PROFIT,
                WINNING_SPIN);
        when(slotGameLogic.placeBet(BET_AMOUNT)).thenReturn(winningGameResult());
        when(slotRequestTransactionClient.requestTransaction(USER_ID, PLAYER_PROFIT))
                .thenReturn(ResponseEntity.ok("Transaction created"));
        when(slotGameFactory.createSlotGame(
                USER_ID,
                BET_AMOUNT,
                true,
                PLAYER_PROFIT,
                WINNING_SPIN)).thenReturn(entity);
        return entity;
    }

    private SlotGameResultView winningGameResult() {
        return new SlotGameResultView(
                BET_AMOUNT,
                WINNING_SPIN,
                true,
                WINNINGS);
    }

    private SlotGameEntity slotGame(
            long id,
            long userId,
            BigDecimal betAmount,
            boolean won,
            BigDecimal amount,
            ThreeReelSpinDTO spinResult) {
        var entity = mock(SlotGameEntity.class);

        when(entity.getId()).thenReturn(id);
        when(entity.getUserId()).thenReturn(userId);
        when(entity.getBetAmount()).thenReturn(betAmount);
        when(entity.isWon()).thenReturn(won);
        when(entity.getAmount()).thenReturn(amount);
        when(entity.getSlotStates()).thenReturn(slotStates(spinResult));

        return entity;
    }

    private List<SlotSymbols> slotStates(ThreeReelSpinDTO spinResult) {
        return List.of(spinResult.first(), spinResult.second(), spinResult.third());
    }
}
