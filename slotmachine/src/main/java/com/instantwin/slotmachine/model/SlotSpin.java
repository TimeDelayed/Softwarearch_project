package com.instantwin.slotmachine.model;

import java.util.List;

import org.springframework.stereotype.Component;

import com.instantwin.slotmachine.contract.model.ISlotSpin;
import com.instantwin.slotmachine.utilities.SlotSymbols;

@Component
public class SlotSpin implements ISlotSpin{

    private final List<SlotSymbols> firstReel;
    private final List<SlotSymbols> secondReel;
    private final List<SlotSymbols> firstReel;
    

    @Override
    public int[] spin() {
        
    }
    
}
