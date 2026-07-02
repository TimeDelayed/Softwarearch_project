package com.instantwin.slotmachine.contract.model;

import com.instantwin.slotmachine.utilities.SlotSymbols;

public interface ISlotSpin {
    SlotSymbols[] spin();
    Float getBetMultiplier();
}
