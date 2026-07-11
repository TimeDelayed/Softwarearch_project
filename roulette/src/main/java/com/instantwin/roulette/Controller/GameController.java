package com.instantwin.roulette.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.instantwin.roulette.contract.controller.IGameController;
import com.instantwin.roulette.contract.handler.IGameHandler;
import com.instantwin.roulette.contract.request.PlayRequest;
import com.instantwin.roulette.contract.view.IGameView;
import com.instantwin.roulette.contract.view.IStatsView;
import com.instantwin.roulette.contract.view.IUserStatsView;

/**
 * DIP: Hängt von IGameHandler (Interface) ab – nicht von der konkreten Implementierung.
 */
@RestController
public class GameController implements IGameController {

    private final IGameHandler gameHandler;

    public GameController(IGameHandler gameHandler) {
        this.gameHandler = gameHandler;
    }

    @Override
    public ResponseEntity<IGameView> play(PlayRequest request) {
        return gameHandler
                .play(request.userId(), request.betAmount(), request.betNumber(), request.betType())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<String> getRules() {
        return ResponseEntity.ok(gameHandler.getRules());
    }

    @Override
    public ResponseEntity<String> getChances() {
        return ResponseEntity.ok(gameHandler.getChances());
    }

    @Override
    public ResponseEntity<IStatsView> getStats() {
        return ResponseEntity.ok(gameHandler.getStats());
    }

    @Override
    public ResponseEntity<IUserStatsView> getUserStats(long userId) {
        return gameHandler.getUserStats(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<List<IGameView>> findAllGames() {
        return ResponseEntity.ok(gameHandler.findAllGames());
    }

    @Override
    public ResponseEntity<IGameView> findGameById(long gameId) {
        return gameHandler.findGameById(gameId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<IGameView> deleteGame(long gameId) {
        return gameHandler.deleteGame(gameId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
