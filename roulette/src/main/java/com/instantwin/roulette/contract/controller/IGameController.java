package com.instantwin.roulette.contract.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.service.annotation.GetExchange;

import com.instantwin.roulette.contract.view.IGameView;

@RequestMapping("/instantwin/roulette/api")
public interface IGameController {
    
    @GetExchange("/games")
    ResponseEntity<List<IGameView>> findeAllGames();




}