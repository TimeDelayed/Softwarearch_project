package com.instantwin.roulette.contract.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.instantwin.roulette.contract.request.PlayRequest;
import com.instantwin.roulette.contract.view.IGameView;

@RequestMapping("/instantwin/roulette/api")
public interface IGameController {

    @GetMapping("/games")
    ResponseEntity<List<IGameView>> findAllGames();

    @PostMapping("/games/play")
    ResponseEntity<IGameView> play(@RequestBody PlayRequest request);
}