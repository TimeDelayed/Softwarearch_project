package com.instantwin.slotmachine.contract.model;

import java.math.BigDecimal;
import java.util.Map;

import com.instantwin.slotmachine.utilities.SlotBetMultipliers;
import com.instantwin.slotmachine.utilities.SlotSymbols;

public interface ISlotConfiguration {
    int getNumberOfReels();
    Map<SlotSymbols, Float> getProbabilities();
    Map<SlotSymbols, SlotBetMultipliers> getDoubleHitReferenceMap();
    Map<SlotSymbols, SlotBetMultipliers> getTripleHitReferenceMap();
    Map<SlotBetMultipliers, BigDecimal> getSymbolMultipliers();
}
