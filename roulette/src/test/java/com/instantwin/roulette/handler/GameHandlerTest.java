package com.instantwin.roulette.handler;

import com.instantwin.roulette.Model.GameEntity;
import com.instantwin.roulette.contract.client.IBankClient;
import com.instantwin.roulette.contract.view.IGameView;
import com.instantwin.roulette.game.BetType;
import com.instantwin.roulette.game.GameResult;
import com.instantwin.roulette.game.RouletteGame;
import com.instantwin.roulette.repostitory.IGameRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameHandlerTest {

    @Mock
    private IGameRepository gameRepository;

    @Mock
    private RouletteGame rouletteGame;

    @Mock
    private IBankClient bankClient;

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
                1L, new BigDecimal("10.00"), 7, BetType.STRAIGHT_UP, 7, new BigDecimal("350.00")
        );
        when(gameRepository.findAll()).thenReturn(List.of(entity));

        List<IGameView> result = gameHandler.findAllGames();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getBetType()).isEqualTo(BetType.STRAIGHT_UP);
        assertThat(result.get(0).getPayout()).isEqualByComparingTo(new BigDecimal("350.00"));
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
    void play_returnsEmpty_whenUserDoesNotExistInBank() {
        when(bankClient.userExists(99L)).thenReturn(false);

        Optional<IGameView> result = gameHandler.play(99L, BigDecimal.TEN, 7, BetType.STRAIGHT_UP);

        assertThat(result).isEmpty();
        verify(rouletteGame, never()).play(any(), anyInt(), any());
        verify(gameRepository, never()).save(any());
    }

    @Test
    void play_savesEntityAndReturnsView_whenUserExists() {
        BigDecimal betAmount = new BigDecimal("10.00");
        BigDecimal payout = new BigDecimal("350.00");
        GameResult gameResult = new GameResult(7, payout);
        GameEntity savedEntity = new GameEntity(1L, betAmount, 7, BetType.STRAIGHT_UP, 7, payout);

        when(bankClient.userExists(1L)).thenReturn(true);
        when(rouletteGame.play(betAmount, 7, BetType.STRAIGHT_UP)).thenReturn(gameResult);
        when(gameRepository.save(any(GameEntity.class))).thenReturn(savedEntity);

        Optional<IGameView> result = gameHandler.play(1L, betAmount, 7, BetType.STRAIGHT_UP);

        assertThat(result).isPresent();
        assertThat(result.get().getPayout()).isEqualByComparingTo(payout);
        assertThat(result.get().getBetType()).isEqualTo(BetType.STRAIGHT_UP);
        assertThat(result.get().getWinningNumber()).isEqualTo(7);
        verify(rouletteGame).play(betAmount, 7, BetType.STRAIGHT_UP);
        verify(gameRepository).save(any(GameEntity.class));
    }

    @Test
    void play_createsTransaction_whenPlayerWins() {
        BigDecimal betAmount = new BigDecimal("10.00");
        BigDecimal payout = new BigDecimal("350.00");
        GameResult gameResult = new GameResult(7, payout);
        GameEntity savedEntity = new GameEntity(1L, betAmount, 7, BetType.STRAIGHT_UP, 7, payout);

        when(bankClient.userExists(1L)).thenReturn(true);
        when(bankClient.createTransaction(1L, payout)).thenReturn(Optional.empty());
        when(rouletteGame.play(betAmount, 7, BetType.STRAIGHT_UP)).thenReturn(gameResult);
        when(gameRepository.save(any(GameEntity.class))).thenReturn(savedEntity);

        gameHandler.play(1L, betAmount, 7, BetType.STRAIGHT_UP);

        verify(bankClient).createTransaction(1L, payout);
    }

    @Test
    void play_doesNotCreateTransaction_whenPlayerLoses() {
        BigDecimal betAmount = new BigDecimal("10.00");
        GameResult lostResult = new GameResult(5, BigDecimal.ZERO);
        GameEntity savedEntity = new GameEntity(1L, betAmount, 17, BetType.STRAIGHT_UP, 5, BigDecimal.ZERO);

        when(bankClient.userExists(1L)).thenReturn(true);
        when(rouletteGame.play(betAmount, 17, BetType.STRAIGHT_UP)).thenReturn(lostResult);
        when(gameRepository.save(any(GameEntity.class))).thenReturn(savedEntity);

        Optional<IGameView> result = gameHandler.play(1L, betAmount, 17, BetType.STRAIGHT_UP);

        assertThat(result).isPresent();
        assertThat(result.get().getPayout()).isEqualByComparingTo(BigDecimal.ZERO);
        verify(bankClient, never()).createTransaction(anyLong(), any());
    }
}
