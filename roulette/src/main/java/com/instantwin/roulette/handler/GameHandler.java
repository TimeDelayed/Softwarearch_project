package com.instantwin.roulette.handler;

import java.util.List;

import org.springframework.stereotype.Service;

import com.instantwin.roulette.contract.handler.IGameHandler;
import com.instantwin.roulette.contract.view.IGameView;
import com.instantwin.roulette.Repostitory.IGameRepository;

@Service
public class GameHandler implements IGameHandler {
    
    private final IGameRepository gameRepository;
    
    public GameHandler(IGameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public List<IGameView> findAllGames() {
        return gameRepository.findAll().stream()
                .map(IGameView::of)
                .toList();
    }
}