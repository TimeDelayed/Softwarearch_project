package com.instantwin.roulette.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.instantwin.roulette.contract.controller.IGameController;
import com.instantwin.roulette.contract.handler.IGameHandler;
import com.instantwin.roulette.contract.request.PlayRequest;
import com.instantwin.roulette.contract.view.IGameView;

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
    public ResponseEntity<List<IGameView>> findAllGames() {
        return ResponseEntity.ok(gameHandler.findAllGames());
    }

    @Override
    public ResponseEntity<IGameView> play(PlayRequest request) {
        IGameView result = gameHandler.play(request.betAmount(), request.betNumber(), request.betType());
        return ResponseEntity.ok(result);
    }
}
