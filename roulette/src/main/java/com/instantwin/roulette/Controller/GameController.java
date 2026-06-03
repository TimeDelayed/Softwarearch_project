package com.instantwin.roulette.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.instantwin.roulette.contract.controller.IGameController;
import com.instantwin.roulette.contract.handler.IGameHandler;
import com.instantwin.roulette.contract.view.IGameView;

public class GameController implements IGameController {
    
private final IGameHandler gameHandler;

    public GameController(IGameHandler gameHandler) {
        this.gameHandler = gameHandler;
    }

    @Override
    public ResponseEntity<List<IGameView>> findeAllGames() {
        List<IGameView> games = gameHandler.findAllGames();
        return ResponseEntity.ok(games);
    }

}
