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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Roulette", description = "Play roulette rounds and retrieve game statistics.")
@RequestMapping("/instantwin/roulette/api")
public interface IGameController {

    @Operation(summary = "Play a round", description = "Spins the wheel and settles the bet. The net result (win or loss) is posted to the bank in a single transaction. The game is only persisted after a successful bank transaction.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Round played and bank transaction successful"),
            @ApiResponse(responseCode = "400", description = "User ID, bet amount, bet number or bet type is invalid"),
            @ApiResponse(responseCode = "404", description = "User not found in the bank"),
            @ApiResponse(responseCode = "500", description = "Bank returned an internal server error")
    })
    @PostMapping("/play")
    ResponseEntity<IGameView> play(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User ID, bet amount, bet number and bet type", required = true)
            @Valid @RequestBody PlayRequest request);

    @Operation(summary = "Get game rules", description = "Returns a plain-text description of all bet types and their rules.")
    @ApiResponse(responseCode = "200", description = "Rules text returned")
    @GetMapping("/info/rules")
    ResponseEntity<String> getRules();

    @Operation(summary = "Get win chances", description = "Returns a table of win probabilities and payout multipliers for every bet type.")
    @ApiResponse(responseCode = "200", description = "Chances and payout table returned")
    @GetMapping("/info/chances")
    ResponseEntity<String> getChances();

    @Operation(summary = "Get global statistics", description = "Returns aggregated statistics across all games ever played: total turnover, cash-out, profit and unique client count.")
    @ApiResponse(responseCode = "200", description = "Statistics returned")
    @GetMapping("/stats")
    ResponseEntity<IStatsView> getStats();

    @Operation(summary = "Get statistics for a user", description = "Returns per-user statistics: total games, winnings, losses and profit/loss figures.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User statistics found"),
            @ApiResponse(responseCode = "404", description = "No games recorded for this user")
    })
    @GetMapping("/stats/user/{user_id}")
    ResponseEntity<IUserStatsView> getUserStats(
            @Parameter(description = "ID of the user", example = "1") @PathVariable("user_id") long userId);

    @Operation(summary = "Get all games", description = "Returns a list of all recorded games. Returns an empty list when no games exist.")
    @ApiResponse(responseCode = "200", description = "List of games returned")
    @GetMapping("/stats/games")
    ResponseEntity<List<IGameView>> findAllGames();

    @Operation(summary = "Get game by ID", description = "Returns the recorded details of a single game.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Game found"),
            @ApiResponse(responseCode = "404", description = "Game not found")
    })
    @GetMapping("/stat/{game_id}")
    ResponseEntity<IGameView> findGameById(
            @Parameter(description = "ID of the game", example = "1") @PathVariable("game_id") long gameId);

    @Operation(summary = "Delete game by ID", description = "Deletes a recorded game and returns it.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Game deleted and returned"),
            @ApiResponse(responseCode = "404", description = "Game not found")
    })
    @DeleteMapping("/stat/{game_id}")
    ResponseEntity<IGameView> deleteGame(
            @Parameter(description = "ID of the game to delete", example = "1") @PathVariable("game_id") long gameId);
}
