package com.instantwin.slotmachine.contract.model;

import java.math.BigDecimal;

import com.instantwin.slotmachine.dto.SlotGameResultDTO;

public interface ISlotGameLogic {
    SlotGameResultDTO placeBet(BigDecimal betAmount);
}
