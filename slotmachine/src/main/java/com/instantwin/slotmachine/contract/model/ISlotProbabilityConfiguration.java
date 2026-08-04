package com.instantwin.slotmachine.contract.model;

import java.util.Map;

import com.instantwin.slotmachine.utilities.SlotSymbols;

public interface ISlotProbabilityConfiguration {

    Map<SlotSymbols, Float> getProbabilities();
}
