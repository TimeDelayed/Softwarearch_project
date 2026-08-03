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
import com.instantwin.slotmachine.view.SlotGameResultView;
import com.instantwin.slotmachine.view.SlotGameView;
import com.instantwin.slotmachine.view.SlotHouseStatsView;

@RequestMapping("/instantwin/slots/api")
public interface ISlotController {
    
    @GetMapping("/info/rules")
    ResponseEntity<String> getGameRules();

    @GetMapping("/info/chances")
    ResponseEntity<String> getSlotChances();

    @GetMapping("/stats")
    ResponseEntity<SlotHouseStatsView> getSlotHouseStats();

    @GetMapping("/stats/user/{userId}")
    ResponseEntity<SlotHouseStatsView> getSlotUserStats(@PathVariable long userId);

    @GetMapping("/stats/games")
    ResponseEntity<List<SlotGameView>> getAllGames();

    @GetMapping("/stat/{gameId}")
    ResponseEntity<SlotGameView> getGameStats(@PathVariable long id);

    @PostMapping("/play")
    ResponseEntity<SlotGameView> playSlotGame(@RequestBody SlotGameRequestBodyDTO slotGameRequestBodyDTO);
    
    @DeleteMapping("/stat/{gameId}")
    ResponseEntity<SlotGameView> deleteGameStats(@PathVariable long id);
}
