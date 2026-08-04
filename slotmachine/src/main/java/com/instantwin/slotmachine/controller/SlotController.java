package com.instantwin.slotmachine.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import com.instantwin.slotmachine.contract.controller.ISlotController;
import com.instantwin.slotmachine.contract.service.ISlotGameService;
import com.instantwin.slotmachine.dto.SlotGameRequestBodyDTO;
import com.instantwin.slotmachine.utilities.SlotResponseMapper;
import com.instantwin.slotmachine.view.SlotClientStatsView;
import com.instantwin.slotmachine.view.SlotGameResultView;
import com.instantwin.slotmachine.view.SlotGameView;
import com.instantwin.slotmachine.view.SlotHouseStatsView;

import jakarta.validation.Valid;

@Validated
@RestController
public class SlotController implements ISlotController {

    private final ISlotGameService slotGameService;

    public SlotController(ISlotGameService slotGameService) {
        this.slotGameService = slotGameService;
    }

    @Override
    public ResponseEntity<String> getGameRules() {
        return ResponseEntity.ok(slotGameService.getGameRules());
    }

    @Override
    public ResponseEntity<String> getSlotChances() {
        return ResponseEntity
                .ok()
                .contentType(MediaType.TEXT_PLAIN)
                .body(slotGameService.getGameChances());
    }

    @Override
    public ResponseEntity<SlotHouseStatsView> getSlotHouseStats() {
        return ResponseEntity.ok(slotGameService.getHouseStats());
    }

    @Override
    public ResponseEntity<SlotClientStatsView> getSlotUserStats(long userId) {
        return SlotResponseMapper.optionalToResponseEntity(slotGameService.getUserStats(userId));
    }

    @Override
    public ResponseEntity<List<SlotGameView>> getAllGames() {
        return ResponseEntity.ok(slotGameService.findAll());
    }

    @Override
    public ResponseEntity<SlotGameView> getGameStats(long gameId) {
        var result = slotGameService.findById(gameId);
        return SlotResponseMapper.optionalToResponseEntity(result);
    }

    @Override
    public ResponseEntity<SlotGameView> playSlotGame(SlotGameRequestBodyDTO slotGameRequestBodyDTO) {
        var result = slotGameService.playSlotGame(slotGameRequestBodyDTO.getUserId(),
                slotGameRequestBodyDTO.getBetAmount());
        return SlotResponseMapper.optionalToResponseEntity(result);
    }

    @Override
    public ResponseEntity<SlotGameView> deleteGameStats(long gameId) {
        var result = slotGameService.deleteSlotGame(gameId);
        return SlotResponseMapper.optionalToResponseEntity(result);
    }

}
