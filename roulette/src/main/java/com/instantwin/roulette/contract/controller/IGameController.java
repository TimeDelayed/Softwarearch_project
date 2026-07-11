package com.instantwin.roulette.contract.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.instantwin.roulette.contract.request.PlayRequest;
import com.instantwin.roulette.contract.view.IGameView;
import com.instantwin.roulette.contract.view.IStatsView;
import com.instantwin.roulette.contract.view.IUserStatsView;

@RequestMapping("/instantwin/roulette/api")
public interface IGameController {

    @PostMapping("/play")
    ResponseEntity<IGameView> play(@RequestBody PlayRequest request);

    @GetMapping("/info/rules")
    ResponseEntity<String> getRules();

    @GetMapping("/info/chances")
    ResponseEntity<String> getChances();

    @GetMapping("/stats")
    ResponseEntity<IStatsView> getStats();

    @GetMapping("/stats/user/{user_id}")
    ResponseEntity<IUserStatsView> getUserStats(@PathVariable("user_id") long userId);

    @GetMapping("/stats/games")
    ResponseEntity<List<IGameView>> findAllGames();

    @GetMapping("/stat/{game_id}")
    ResponseEntity<IGameView> findGameById(@PathVariable("game_id") long gameId);

    @DeleteMapping("/stat/{game_id}")
    ResponseEntity<IGameView> deleteGame(@PathVariable("game_id") long gameId);
}