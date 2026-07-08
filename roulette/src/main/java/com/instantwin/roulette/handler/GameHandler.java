package com.instantwin.roulette.handler;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.instantwin.roulette.Model.GameEntity;
import com.instantwin.roulette.Model.GameFactory;
import com.instantwin.roulette.View.GameView;
import com.instantwin.roulette.contract.handler.IGameHandler;
import com.instantwin.roulette.contract.view.IGameView;
import com.instantwin.roulette.game.BetType;
import com.instantwin.roulette.game.GameResult;
import com.instantwin.roulette.game.RouletteGame;
import com.instantwin.roulette.repostitory.IGameRepository;

/**
 * DIP: Hängt von Interfaces (IGameRepository) und Spring-Component (RouletteGame) ab.
 * SRP: Orchestriert Spiellogik und Persistenz – delegiert beides an Spezialisten.
 */
@Service
public class GameHandler implements IGameHandler {

    private final IGameRepository gameRepository;
    private final RouletteGame rouletteGame;

    public GameHandler(IGameRepository gameRepository, RouletteGame rouletteGame) {
        this.gameRepository = gameRepository;
        this.rouletteGame = rouletteGame;
    }

    @Override
    public List<IGameView> findAllGames() {
        return gameRepository.findAll().stream()
                .<IGameView>map(GameView::of)
                .toList();
    }

    @Override
    @Transactional
    public IGameView play(BigDecimal betAmount, int betNumber, BetType betType) {
        GameResult result = rouletteGame.play(betAmount, betNumber, betType);
        GameEntity entity = GameFactory.create(betAmount, betNumber, betType, result);
        return GameView.of(gameRepository.save(entity));
    }
}