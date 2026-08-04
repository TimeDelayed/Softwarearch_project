package com.instantwin.slotmachine.contract.service;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;

import com.instantwin.slotmachine.view.SlotClientStatsView;
import com.instantwin.slotmachine.view.SlotGameView;
import com.instantwin.slotmachine.view.SlotHouseStatsView;

public interface ISlotGameService {
    List<SlotGameView> findAll();

    List<SlotGameView> findAllByUserId(long userId);

    Optional<SlotGameView> findById(long gameId);

    Optional<SlotGameView> playSlotGame(long userId, BigDecimal amount);

    Optional<SlotGameView> deleteSlotGame(long gameId);

    String getGameRules();

    String getGameChances();

    SlotHouseStatsView getHouseStats();

    SlotClientStatsView getUserStats(long userId);
}
