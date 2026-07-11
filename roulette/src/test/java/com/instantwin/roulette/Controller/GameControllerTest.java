package com.instantwin.roulette.Controller;

import com.instantwin.roulette.Model.GameEntity;
import com.instantwin.roulette.View.GameView;
import com.instantwin.roulette.contract.handler.IGameHandler;
import com.instantwin.roulette.contract.request.PlayRequest;
import com.instantwin.roulette.contract.view.IGameView;
import com.instantwin.roulette.game.BetType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
                .thenReturn(Optional.of(view));

        ResponseEntity<IGameView> response = gameController.play(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getPayout()).isEqualByComparingTo(new BigDecimal("350.00"));
    }

    @Test
    void play_returns404_whenUserNotFoundInBank() {
        PlayRequest request = new PlayRequest(99L, new BigDecimal("10.00"), 5, BetType.STRAIGHT_UP);
        when(gameHandler.play(request.userId(), request.betAmount(), request.betNumber(), request.betType()))
                .thenReturn(Optional.empty());

        ResponseEntity<IGameView> response = gameController.play(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void play_delegatesToHandlerWithCorrectArguments() {
        PlayRequest request = new PlayRequest(2L, new BigDecimal("20.00"), 3, BetType.EVEN);
        IGameView view = GameView.of(
                new GameEntity(2L, new BigDecimal("20.00"), 3, BetType.EVEN, 6, new BigDecimal("40.00"))
        );
        when(gameHandler.play(request.userId(), request.betAmount(), request.betNumber(), request.betType()))
                .thenReturn(Optional.of(view));

        gameController.play(request);

        verify(gameHandler).play(2L, new BigDecimal("20.00"), 3, BetType.EVEN);
    }
}
