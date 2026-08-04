package com.instantwin.slotmachine.contract.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.instantwin.slotmachine.dto.SlotGameRequestBodyDTO;
import com.instantwin.slotmachine.view.SlotClientStatsView;
import com.instantwin.slotmachine.view.SlotGameView;
import com.instantwin.slotmachine.view.SlotHouseStatsView;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Slot Machine", description = "Operations for playing slot games and retrieving game information and statistics.")
@RequestMapping("/instantwin/slots/api")
public interface ISlotController {

    @Operation(summary = "Get game rules", description = "Returns the rules and payout information of the slot machine as text.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Game rules successfully retrieved"),
            @ApiResponse(responseCode = "500", description = "Game rules are unavailable")
    })
    @GetMapping("/info/rules")
    ResponseEntity<String> getGameRules();

    @Operation(summary = "Get slot chances", description = "Returns symbol probabilities, win probabilities and expected return information as text.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Slot chances successfully retrieved"),
            @ApiResponse(responseCode = "500", description = "Slot chances are unavailable")
    })
    @GetMapping("/info/chances")
    ResponseEntity<String> getSlotChances();

    @Operation(summary = "Get house statistics", description = "Returns aggregated statistics for all slot games, including turnover, cashout and house profit.")
    @ApiResponse(responseCode = "200", description = "House statistics successfully retrieved")
    @GetMapping("/stats")
    ResponseEntity<SlotHouseStatsView> getSlotHouseStats();

    @Operation(summary = "Get user slot statistics", description = "Returns aggregated slot statistics for the specified user when recorded games exist.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User slot statistics successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "No slot statistics found for the user")
    })
    @GetMapping("/stats/user/{userId}")
    ResponseEntity<SlotClientStatsView> getSlotUserStats(
            @Parameter(description = "ID used to select the user's stored slot games", example = "1") @PathVariable long userId);

    @Operation(summary = "Get all slot games", description = "Returns all stored slot game results.")
    @ApiResponse(responseCode = "200", description = "Slot games successfully retrieved")
    @GetMapping("/stats/games")
    ResponseEntity<List<SlotGameView>> getAllGames();

    @Operation(summary = "Get slot game by ID", description = "Returns the stored result of a specific slot game.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Slot game found"),
            @ApiResponse(responseCode = "404", description = "Slot game not found")
    })
    @GetMapping("/stat/{gameId}")
    ResponseEntity<SlotGameView> getGameStats(
            @Parameter(description = "ID of the slot game", example = "1") @PathVariable long gameId);

    @Operation(summary = "Play slot game", description = "Plays a slot game for the specified user, requests the resulting bank transaction and stores the game result.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Slot game successfully played"),
            @ApiResponse(responseCode = "400", description = "User ID or bet amount is invalid"),
            @ApiResponse(responseCode = "404", description = "User not found or transaction rejected"),
            @ApiResponse(responseCode = "500", description = "Slot configuration is invalid or game processing failed")
    })
    @PostMapping("/play")
    ResponseEntity<SlotGameView> playSlotGame(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User ID and positive bet amount for the slot game", required = true)
            @RequestBody @Valid SlotGameRequestBodyDTO slotGameRequestBodyDTO);

    @Operation(summary = "Delete slot game", description = "Deletes the stored result of a specific slot game.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Slot game successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Slot game not found")
    })
    @DeleteMapping("/stat/{gameId}")
    ResponseEntity<SlotGameView> deleteGameStats(
            @Parameter(description = "ID of the slot game to delete", example = "1") @PathVariable long gameId);
}
