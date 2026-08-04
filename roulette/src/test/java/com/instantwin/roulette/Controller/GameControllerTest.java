package com.instantwin.roulette.Controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.instantwin.roulette.Model.GameEntity;
import com.instantwin.roulette.View.GameView;
import com.instantwin.roulette.contract.handler.IGameHandler;
import com.instantwin.roulette.contract.request.PlayRequest;
import com.instantwin.roulette.contract.view.IGameView;
import com.instantwin.roulette.contract.view.IStatsView;
import com.instantwin.roulette.contract.view.IUserStatsView;
import com.instantwin.roulette.game.BetType;

@ExtendWith(MockitoExtension.class)
class GameControllerTest {

    @Mock
    private IGameHandler gameHandler;

    @InjectMocks
    private GameController gameController;

    // -------------------------------------------------------------------------
    // findAllGames
    // -------------------------------------------------------------------------

    @Test
    void findAllGames_returns200_withEmptyBody_whenNoGamesExist() {
        when(gameHandler.findAllGames()).thenReturn(List.of());

        ResponseEntity<List<IGameView>> response = gameController.findAllGames();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEmpty();
    }

    @Test
    void findAllGames_returns200_withAllGamesInBody() {
        IGameView view = GameView.of(
                new GameEntity(1L, new BigDecimal("10.00"), 5, BetType.RED, 3, new BigDecimal("20.00"))
        );
        when(gameHandler.findAllGames()).thenReturn(List.of(view));

        ResponseEntity<List<IGameView>> response = gameController.findAllGames();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getBetType()).isEqualTo(BetType.RED);
    }

    @Test
    void findAllGames_delegatesToHandler() {
        when(gameHandler.findAllGames()).thenReturn(List.of());

        gameController.findAllGames();

        verify(gameHandler).findAllGames();
    }

    // -------------------------------------------------------------------------
    // play
    // -------------------------------------------------------------------------

    @Test
    void play_returns200_withResultInBody() {
        PlayRequest request = new PlayRequest(1L, new BigDecimal("10.00"), 5, BetType.STRAIGHT_UP);
        IGameView view = GameView.of(
                new GameEntity(1L, new BigDecimal("10.00"), 5, BetType.STRAIGHT_UP, 5, new BigDecimal("350.00"))
        );
        when(gameHandler.play(request.userId(), request.betAmount(), request.betNumber(), request.betType()))
                .thenReturn(ResponseEntity.ok(view));

        ResponseEntity<IGameView> response = gameController.play(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getPayout()).isEqualByComparingTo(new BigDecimal("350.00"));
    }

    @Test
    void play_returns404_whenBankReturnsNotFound() {
        PlayRequest request = new PlayRequest(99L, new BigDecimal("10.00"), 5, BetType.STRAIGHT_UP);
        when(gameHandler.play(request.userId(), request.betAmount(), request.betNumber(), request.betType()))
                .thenReturn(ResponseEntity.notFound().build());

        ResponseEntity<IGameView> response = gameController.play(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void play_returns500_whenBankHasInternalError() {
        PlayRequest request = new PlayRequest(1L, new BigDecimal("10.00"), 5, BetType.STRAIGHT_UP);
        when(gameHandler.play(request.userId(), request.betAmount(), request.betNumber(), request.betType()))
                .thenReturn(ResponseEntity.internalServerError().build());

        ResponseEntity<IGameView> response = gameController.play(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    void play_delegatesToHandlerWithCorrectArguments() {
        PlayRequest request = new PlayRequest(2L, new BigDecimal("20.00"), 3, BetType.EVEN);
        IGameView view = GameView.of(
                new GameEntity(2L, new BigDecimal("20.00"), 3, BetType.EVEN, 6, new BigDecimal("40.00"))
        );
        when(gameHandler.play(request.userId(), request.betAmount(), request.betNumber(), request.betType()))
                .thenReturn(ResponseEntity.ok(view));

        gameController.play(request);

        verify(gameHandler).play(2L, new BigDecimal("20.00"), 3, BetType.EVEN);
    }

    // -------------------------------------------------------------------------
    // findGameById
    // -------------------------------------------------------------------------

    @Test
    void findGameById_returns200_withGameInBody() {
        IGameView view = GameView.of(
                new GameEntity(1L, new BigDecimal("10.00"), 7, BetType.STRAIGHT_UP, 7, new BigDecimal("350.00"))
        );
        when(gameHandler.findGameById(1L)).thenReturn(Optional.of(view));

        ResponseEntity<IGameView> response = gameController.findGameById(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getBetType()).isEqualTo(BetType.STRAIGHT_UP);
    }

    @Test
    void findGameById_returns404_whenGameNotFound() {
        when(gameHandler.findGameById(99L)).thenReturn(Optional.empty());

        ResponseEntity<IGameView> response = gameController.findGameById(99L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void findGameById_delegatesToHandler() {
        when(gameHandler.findGameById(1L)).thenReturn(Optional.empty());

        gameController.findGameById(1L);

        verify(gameHandler).findGameById(1L);
    }

    // -------------------------------------------------------------------------
    // deleteGame
    // -------------------------------------------------------------------------

    @Test
    void deleteGame_returns200_withDeletedGameInBody() {
        IGameView view = GameView.of(
                new GameEntity(1L, new BigDecimal("10.00"), 3, BetType.RED, 1, new BigDecimal("20.00"))
        );
        when(gameHandler.deleteGame(1L)).thenReturn(Optional.of(view));

        ResponseEntity<IGameView> response = gameController.deleteGame(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getBetType()).isEqualTo(BetType.RED);
    }

    @Test
    void deleteGame_returns404_whenGameNotFound() {
        when(gameHandler.deleteGame(99L)).thenReturn(Optional.empty());

        ResponseEntity<IGameView> response = gameController.deleteGame(99L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void deleteGame_delegatesToHandler() {
        when(gameHandler.deleteGame(1L)).thenReturn(Optional.empty());

        gameController.deleteGame(1L);

        verify(gameHandler).deleteGame(1L);
    }

    // -------------------------------------------------------------------------
    // getRules
    // -------------------------------------------------------------------------

    @Test
    void getRules_returns200_withRulesText() {
        when(gameHandler.getRules()).thenReturn("rules text");

        ResponseEntity<String> response = gameController.getRules();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("rules text");
    }

    @Test
    void getRules_delegatesToHandler() {
        when(gameHandler.getRules()).thenReturn("rules text");

        gameController.getRules();

        verify(gameHandler).getRules();
    }

    // -------------------------------------------------------------------------
    // getChances
    // -------------------------------------------------------------------------

    @Test
    void getChances_returns200_withChancesText() {
        when(gameHandler.getChances()).thenReturn("chances text");

        ResponseEntity<String> response = gameController.getChances();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("chances text");
    }

    @Test
    void getChances_delegatesToHandler() {
        when(gameHandler.getChances()).thenReturn("chances text");

        gameController.getChances();

        verify(gameHandler).getChances();
    }

    // -------------------------------------------------------------------------
    // getStats
    // -------------------------------------------------------------------------

    @Test
    void getStats_returns200_withStatsBody() {
        IStatsView stats = mock(IStatsView.class);
        when(gameHandler.getStats()).thenReturn(stats);

        ResponseEntity<IStatsView> response = gameController.getStats();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(stats);
    }

    @Test
    void getStats_delegatesToHandler() {
        IStatsView stats = mock(IStatsView.class);
        when(gameHandler.getStats()).thenReturn(stats);

        gameController.getStats();

        verify(gameHandler).getStats();
    }

    // -------------------------------------------------------------------------
    // getUserStats
    // -------------------------------------------------------------------------

    @Test
    void getUserStats_returns200_withUserStatsBody() {
        IUserStatsView userStats = mock(IUserStatsView.class);
        when(gameHandler.getUserStats(1L)).thenReturn(Optional.of(userStats));

        ResponseEntity<IUserStatsView> response = gameController.getUserStats(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(userStats);
    }

    @Test
    void getUserStats_returns404_whenUserHasNoGames() {
        when(gameHandler.getUserStats(99L)).thenReturn(Optional.empty());

        ResponseEntity<IUserStatsView> response = gameController.getUserStats(99L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void getUserStats_delegatesToHandler() {
        when(gameHandler.getUserStats(1L)).thenReturn(Optional.empty());

        gameController.getUserStats(1L);

        verify(gameHandler).getUserStats(1L);
    }
}
