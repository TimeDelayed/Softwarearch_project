package com.instantwin.roulette.contract.handler;

import java.util.List;
import com.instantwin.roulette.contract.view.IGameView;

public interface IGameHandler {
    
    List<IGameView> findAllGames();

}
