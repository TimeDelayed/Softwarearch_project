package com.instantwin.slotmachine.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import com.instantwin.slotmachine.contract.controller.ISlotController;
import com.instantwin.slotmachine.contract.service.ISlotGameService;
import com.instantwin.slotmachine.dto.SlotGameRequestBodyDTO;
import com.instantwin.slotmachine.utilities.SlotResponseMapper;
import com.instantwin.slotmachine.view.SlotGameResultView;
import com.instantwin.slotmachine.view.SlotGameView;
import com.instantwin.slotmachine.view.SlotHouseStatsView;

import jakarta.validation.Valid;

@RestController
@Validated
public class SlotController implements ISlotController {

    private final ISlotGameService slotGameService;

    public SlotController(ISlotGameService slotGameService) {
        this.slotGameService = slotGameService;
    }

    @Override
    public ResponseEntity<String> getGameRules() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSlotChances'");
    }

    @Override
    public ResponseEntity<String> getSlotChances() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSlotChances'");
    }

    @Override
    public ResponseEntity<SlotHouseStatsView> getSlotHouseStats() {
        return ResponseEntity.ok(slotGameService.getHouseStats());
    }

    @Override
    public ResponseEntity<SlotHouseStatsView> getSlotUserStats(long userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSlotUserStats'");
    }

    @Override
    public ResponseEntity<List<SlotGameView>> getAllGames() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllGames'");
    }

    @Override
    public ResponseEntity<SlotGameView> getGameStats(long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getGameStats'");
    }

    @Override
    public ResponseEntity<SlotGameResultView> playSlotGame(SlotGameRequestBodyDTO slotGameRequestBodyDTO) {
        var result = slotGameService.playSlotGame(slotGameRequestBodyDTO.getUserId(), slotGameRequestBodyDTO.getBetAmount());

        return ResponseEntity.ok(SlotResponseMapper.optionalToResponseEntity(result));
    }

    @Override
    public ResponseEntity<SlotGameView> deleteGameStats(long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteGameStats'");
    }
    
}
