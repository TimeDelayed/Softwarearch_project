package com.instantwin.slotmachine.contract.model;

import java.math.BigDecimal;

import com.instantwin.slotmachine.view.SlotGameResultView;

public interface ISlotGameLogic {
    SlotGameResultView placeBet(BigDecimal betAmount);
}
