package com.instantwin.roulette.handler;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.instantwin.roulette.contract.client.IBankClient;
import com.instantwin.roulette.contract.game.IRouletteGame;
import com.instantwin.roulette.contract.model.IGameFactory;
import com.instantwin.roulette.contract.view.IGameView;
import com.instantwin.roulette.contract.view.IStatsView;
import com.instantwin.roulette.contract.view.IUserStatsView;
import org.springframework.http.ResponseEntity;
import com.instantwin.roulette.game.BetType;
import com.instantwin.roulette.game.GameResult;
import com.instantwin.roulette.model.GameEntity;
import com.instantwin.roulette.repostitory.IGameRepository;
import com.instantwin.roulette.utilities.BankTransactionFailedException;

@ExtendWith(MockitoExtension.class)
class GameHandlerTest {

    @Mock
    private IGameRepository gameRepository;

    @Mock
    private IRouletteGame rouletteGame;

    @Mock
    private IBankClient bankClient;

    @Mock
    private IGameFactory gameFactory;

    @InjectMocks
    private GameHandler gameHandler;

    // -------------------------------------------------------------------------
    // findAllGames
    // -------------------------------------------------------------------------

    @Test
    void findAllGames_returnsEmptyList_whenNoGamesExist() {
        when(gameRepository.findAll()).thenReturn(List.of());

        List<IGameView> result = gameHandler.findAllGames();

        assertThat(result).isEmpty();
    }

    @Test
    void findAllGames_returnsMappedView_forEachStoredEntity() {
        GameEntity entity = new GameEntity(
                1L, new BigDecimal("10.00"), 7, BetType.STRAIGHT_UP, 7, new BigDecimal("360.00")
        );
        when(gameRepository.findAll()).thenReturn(List.of(entity));

        List<IGameView> result = gameHandler.findAllGames();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getBetType()).isEqualTo(BetType.STRAIGHT_UP);
        assertThat(result.get(0).getPayout()).isEqualByComparingTo(new BigDecimal("360.00"));
    }

    @Test
    void findAllGames_returnsViewForEveryEntity() {
        GameEntity a = new GameEntity(1L, BigDecimal.TEN, 1, BetType.RED, 3, new BigDecimal("20"));
        GameEntity b = new GameEntity(2L, BigDecimal.TEN, 0, BetType.EVEN, 4, new BigDecimal("20"));
        when(gameRepository.findAll()).thenReturn(List.of(a, b));

        List<IGameView> result = gameHandler.findAllGames();

        assertThat(result).hasSize(2);
    }

    // -------------------------------------------------------------------------
    // play
    // -------------------------------------------------------------------------

    @Test
    void play_returnsGame_andSavesEntity_whenTransactionSucceeds() {
        BigDecimal betAmount = new BigDecimal("10.00");
        BigDecimal payout = new BigDecimal("360.00");
        BigDecimal netAmount = new BigDecimal("350.00");
        GameResult gameResult = new GameResult(7, payout);
        GameEntity savedEntity = new GameEntity(1L, betAmount, 7, BetType.STRAIGHT_UP, 7, payout);

        when(rouletteGame.play(betAmount, 7, BetType.STRAIGHT_UP)).thenReturn(gameResult);
        when(bankClient.requestTransaction(1L, netAmount)).thenReturn(ResponseEntity.ok("ok"));
        when(gameFactory.create(1L, betAmount, 7, BetType.STRAIGHT_UP, gameResult)).thenReturn(savedEntity);
        when(gameRepository.save(savedEntity)).thenReturn(savedEntity);

        Optional<IGameView> response = gameHandler.play(1L, betAmount, 7, BetType.STRAIGHT_UP);

        assertThat(response).isPresent();
        assertThat(response.get().getPayout()).isEqualByComparingTo(payout);
        verify(rouletteGame).play(betAmount, 7, BetType.STRAIGHT_UP);
        verify(gameFactory).create(1L, betAmount, 7, BetType.STRAIGHT_UP, gameResult);
        verify(gameRepository).save(savedEntity);
    }

    @Test
    void play_sendsNegativeNetAmount_andSavesEntity_onLoss() {
        BigDecimal betAmount = new BigDecimal("10.00");
        BigDecimal negativeNet = new BigDecimal("-10.00");
        GameResult lostResult = new GameResult(5, BigDecimal.ZERO);
        GameEntity savedEntity = new GameEntity(1L, betAmount, 17, BetType.STRAIGHT_UP, 5, BigDecimal.ZERO);

        when(rouletteGame.play(betAmount, 17, BetType.STRAIGHT_UP)).thenReturn(lostResult);
        when(bankClient.requestTransaction(1L, negativeNet)).thenReturn(ResponseEntity.ok("ok"));
        when(gameFactory.create(1L, betAmount, 17, BetType.STRAIGHT_UP, lostResult)).thenReturn(savedEntity);
        when(gameRepository.save(savedEntity)).thenReturn(savedEntity);

        Optional<IGameView> response = gameHandler.play(1L, betAmount, 17, BetType.STRAIGHT_UP);

        assertThat(response).isPresent();
        verify(bankClient).requestTransaction(1L, negativeNet);
        verify(gameFactory).create(1L, betAmount, 17, BetType.STRAIGHT_UP, lostResult);
        verify(gameRepository).save(savedEntity);
    }

    @Test
    void play_returnsEmpty_andDoesNotSave_whenUserDoesNotExist() {
        BigDecimal betAmount = new BigDecimal("10.00");
        GameResult gameResult = new GameResult(7, new BigDecimal("360.00"));

        when(rouletteGame.play(betAmount, 7, BetType.STRAIGHT_UP)).thenReturn(gameResult);
        when(bankClient.requestTransaction(anyLong(), any())).thenReturn(ResponseEntity.notFound().build());

        Optional<IGameView> response = gameHandler.play(99L, betAmount, 7, BetType.STRAIGHT_UP);

        assertThat(response).isEmpty();
        verify(gameFactory, never()).create(anyLong(), any(), anyInt(), any(), any());
        verify(gameRepository, never()).save(any());
    }

    @Test
    void play_throws_andDoesNotSave_whenBankHasInternalError() {
        BigDecimal betAmount = new BigDecimal("10.00");
        GameResult gameResult = new GameResult(7, new BigDecimal("360.00"));

        when(rouletteGame.play(betAmount, 7, BetType.STRAIGHT_UP)).thenReturn(gameResult);
        when(bankClient.requestTransaction(anyLong(), any())).thenReturn(ResponseEntity.internalServerError().build());

        assertThatThrownBy(() -> gameHandler.play(1L, betAmount, 7, BetType.STRAIGHT_UP))
                .isInstanceOf(BankTransactionFailedException.class);
        verify(gameFactory, never()).create(anyLong(), any(), anyInt(), any(), any());
        verify(gameRepository, never()).save(any());
    }

    // -------------------------------------------------------------------------
    // findGameById
    // -------------------------------------------------------------------------

    @Test
    void findGameById_returnsView_whenGameExists() {
        GameEntity entity = new GameEntity(
                1L, new BigDecimal("10.00"), 7, BetType.STRAIGHT_UP, 7, new BigDecimal("360.00")
        );
        when(gameRepository.findById(1L)).thenReturn(Optional.of(entity));

        Optional<IGameView> result = gameHandler.findGameById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getBetType()).isEqualTo(BetType.STRAIGHT_UP);
        assertThat(result.get().getPayout()).isEqualByComparingTo(new BigDecimal("360.00"));
    }

    @Test
    void findGameById_returnsEmpty_whenGameDoesNotExist() {
        when(gameRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<IGameView> result = gameHandler.findGameById(99L);

        assertThat(result).isEmpty();
    }

    // -------------------------------------------------------------------------
    // deleteGame
    // -------------------------------------------------------------------------

    @Test
    void deleteGame_returnsView_andDeletesEntity_whenGameExists() {
        GameEntity entity = new GameEntity(1L, BigDecimal.TEN, 3, BetType.RED, 1, new BigDecimal("20"));
        when(gameRepository.findById(1L)).thenReturn(Optional.of(entity));

        Optional<IGameView> result = gameHandler.deleteGame(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getBetType()).isEqualTo(BetType.RED);
        verify(gameRepository).deleteById(any());
    }

    @Test
    void deleteGame_returnsEmpty_andDoesNotDelete_whenGameDoesNotExist() {
        when(gameRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<IGameView> result = gameHandler.deleteGame(99L);

        assertThat(result).isEmpty();
        verify(gameRepository, never()).deleteById(any());
    }

    // -------------------------------------------------------------------------
    // getRules
    // -------------------------------------------------------------------------

    @Test
    void getRules_returnsNonEmptyText_containingBetTypes() {
        String rules = gameHandler.getRules();

        assertThat(rules).isNotBlank();
        assertThat(rules).contains("STRAIGHT_UP");
        assertThat(rules).contains("DOZEN");
    }

    // -------------------------------------------------------------------------
    // getChances
    // -------------------------------------------------------------------------

    @Test
    void getChances_returnsNonEmptyText_containingBetTypes() {
        String chances = gameHandler.getChances();

        assertThat(chances).isNotBlank();
        assertThat(chances).contains("STRAIGHT_UP");
        assertThat(chances).contains("COLUMN");
        assertThat(chances).contains("bet × 36");
    }

    // -------------------------------------------------------------------------
    // getStats
    // -------------------------------------------------------------------------

    @Test
    void getStats_returnsZeroedStats_whenNoGamesExist() {
        when(gameRepository.findAll()).thenReturn(List.of());

        IStatsView stats = gameHandler.getStats();

        assertThat(stats.getTotalGamesCount()).isZero();
        assertThat(stats.getTotalClientCount()).isZero();
        assertThat(stats.getTotalProfit()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(stats.getTotalCashOut()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(stats.getTotalTurnover()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void getStats_calculatesCorrectValues_forGivenGames() {
        GameEntity win = new GameEntity(1L, new BigDecimal("10"), 7, BetType.STRAIGHT_UP, 7, new BigDecimal("360"));
        GameEntity loss = new GameEntity(2L, new BigDecimal("10"), 5, BetType.RED, 2, BigDecimal.ZERO);
        when(gameRepository.findAll()).thenReturn(List.of(win, loss));

        IStatsView stats = gameHandler.getStats();

        assertThat(stats.getTotalGamesCount()).isEqualTo(2);
        assertThat(stats.getTotalClientCount()).isEqualTo(2);
        assertThat(stats.getTotalTurnover()).isEqualByComparingTo(new BigDecimal("20"));
        assertThat(stats.getTotalCashOut()).isEqualByComparingTo(new BigDecimal("360"));
        assertThat(stats.getTotalProfit()).isEqualByComparingTo(new BigDecimal("-340"));
    }

    @Test
    void getStats_countsDistinctClients() {
        GameEntity g1 = new GameEntity(1L, BigDecimal.TEN, 0, BetType.RED, 3, new BigDecimal("20"));
        GameEntity g2 = new GameEntity(1L, BigDecimal.TEN, 0, BetType.RED, 2, BigDecimal.ZERO);
        when(gameRepository.findAll()).thenReturn(List.of(g1, g2));

        IStatsView stats = gameHandler.getStats();

        assertThat(stats.getTotalClientCount()).isEqualTo(1);
        assertThat(stats.getTotalGamesCount()).isEqualTo(2);
    }

    // -------------------------------------------------------------------------
    // getUserStats
    // -------------------------------------------------------------------------

    @Test
    void getUserStats_returnsEmpty_whenUserHasNoGames() {
        when(gameRepository.findByUserId(99L)).thenReturn(List.of());

        Optional<IUserStatsView> result = gameHandler.getUserStats(99L);

        assertThat(result).isEmpty();
    }

    @Test
    void getUserStats_calculatesCorrectValues_forWinAndLoss() {
        GameEntity win = new GameEntity(1L, new BigDecimal("10"), 7, BetType.STRAIGHT_UP, 7, new BigDecimal("360"));
        GameEntity loss = new GameEntity(1L, new BigDecimal("10"), 5, BetType.RED, 2, BigDecimal.ZERO);
        when(gameRepository.findByUserId(1L)).thenReturn(List.of(win, loss));

        Optional<IUserStatsView> result = gameHandler.getUserStats(1L);

        assertThat(result).isPresent();
        IUserStatsView stats = result.get();
        assertThat(stats.getTotalGamesCount()).isEqualTo(2);
        assertThat(stats.getTotalWinnings()).isEqualByComparingTo(new BigDecimal("350"));
        assertThat(stats.getTotalLosses()).isEqualByComparingTo(new BigDecimal("10"));
        assertThat(stats.getTotalHouseTurnoverFromClient()).isEqualByComparingTo(new BigDecimal("20"));
    }

    @Test
    void getUserStats_returnsCorrectClientId() {
        GameEntity game = new GameEntity(5L, BigDecimal.TEN, 0, BetType.BLACK, 1, BigDecimal.ZERO);
        when(gameRepository.findByUserId(5L)).thenReturn(List.of(game));

        Optional<IUserStatsView> result = gameHandler.getUserStats(5L);

        assertThat(result).isPresent();
        assertThat(result.get().getClient()).isEqualTo(5L);
    }
}
