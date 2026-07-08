package com.instantwin.roulette.handler;

import com.instantwin.roulette.Model.GameEntity;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameHandlerTest {

    @Mock
    private IGameRepository gameRepository;

    @Mock
    private RouletteGame rouletteGame;

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
                new BigDecimal("10.00"), 7, BetType.STRAIGHT_UP, 7, new BigDecimal("350.00")
        );
        when(gameRepository.findAll()).thenReturn(List.of(entity));

        List<IGameView> result = gameHandler.findAllGames();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getBetType()).isEqualTo(BetType.STRAIGHT_UP);
        assertThat(result.get(0).getPayout()).isEqualByComparingTo(new BigDecimal("350.00"));
    }

    @Test
    void findAllGames_returnsViewForEveryEntity() {
        GameEntity a = new GameEntity(BigDecimal.TEN, 1, BetType.RED, 3, new BigDecimal("20"));
        GameEntity b = new GameEntity(BigDecimal.TEN, 0, BetType.EVEN, 4, new BigDecimal("20"));
        when(gameRepository.findAll()).thenReturn(List.of(a, b));

        List<IGameView> result = gameHandler.findAllGames();

        assertThat(result).hasSize(2);
    }

    // -------------------------------------------------------------------------
    // play
    // -------------------------------------------------------------------------

    @Test
    void play_callsRouletteGameAndSavesEntity() {
        BigDecimal betAmount = new BigDecimal("10.00");
        BigDecimal payout = new BigDecimal("350.00");
        GameResult gameResult = new GameResult(7, payout);
        GameEntity savedEntity = new GameEntity(betAmount, 7, BetType.STRAIGHT_UP, 7, payout);

        when(rouletteGame.play(betAmount, 7, BetType.STRAIGHT_UP)).thenReturn(gameResult);
        when(gameRepository.save(any(GameEntity.class))).thenReturn(savedEntity);

        IGameView view = gameHandler.play(betAmount, 7, BetType.STRAIGHT_UP);

        verify(rouletteGame).play(betAmount, 7, BetType.STRAIGHT_UP);
        verify(gameRepository).save(any(GameEntity.class));
        assertThat(view.getPayout()).isEqualByComparingTo(payout);
        assertThat(view.getBetType()).isEqualTo(BetType.STRAIGHT_UP);
        assertThat(view.getWinningNumber()).isEqualTo(7);
    }

    @Test
    void play_withLosingBet_returnsZeroPayout() {
        BigDecimal betAmount = new BigDecimal("10.00");
        GameResult lostResult = new GameResult(5, BigDecimal.ZERO);
        GameEntity savedEntity = new GameEntity(betAmount, 17, BetType.STRAIGHT_UP, 5, BigDecimal.ZERO);

        when(rouletteGame.play(betAmount, 17, BetType.STRAIGHT_UP)).thenReturn(lostResult);
        when(gameRepository.save(any(GameEntity.class))).thenReturn(savedEntity);

        IGameView view = gameHandler.play(betAmount, 17, BetType.STRAIGHT_UP);

        assertThat(view.getPayout()).isEqualByComparingTo(BigDecimal.ZERO);
    }
}
