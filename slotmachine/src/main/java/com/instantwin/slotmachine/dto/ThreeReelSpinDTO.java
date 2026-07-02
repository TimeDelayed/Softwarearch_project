package com.instantwin.slotmachine.dto;

import com.instantwin.slotmachine.utilities.SlotSymbols;

public record ThreeReelSpinDTO(SlotSymbols first, SlotSymbols second, SlotSymbols third) {
    
    public SlotSymbols getFirst() {
        return first;
    }

    public SlotSymbols getSecond() {
        return second;
    }

    public SlotSymbols getThird() {
        return third;
    }
}
