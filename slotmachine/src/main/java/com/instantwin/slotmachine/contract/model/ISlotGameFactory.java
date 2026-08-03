package com.instantwin.slotmachine.contract.model;

import java.math.BigDecimal;

import com.instantwin.slotmachine.dto.ThreeReelSpinDTO;
import com.instantwin.slotmachine.model.SlotGameEntity;

public interface ISlotGameFactory {
    SlotGameEntity createSlotGame(Long userId, BigDecimal betAmount, boolean won, BigDecimal amount,
            ThreeReelSpinDTO spinResult);
}