package com.instantwin.slotmachine.model;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.instantwin.slotmachine.contract.model.ISlotGameFactory;
import com.instantwin.slotmachine.dto.ThreeReelSpinDTO;

@Component
public class SlotGameFactory implements ISlotGameFactory {

    @Override
    public SlotGameEntity createSlotGame(Long userId, BigDecimal betAmount, boolean won, BigDecimal amount,
            ThreeReelSpinDTO spinResult) {
        return SlotGameEntity.of(userId, betAmount, won, amount, spinResult);
    }

}
