package com.instantwin.slotmachine.view;

import java.math.BigDecimal;
import java.util.List;

import com.instantwin.slotmachine.model.SlotGameEntity;
import com.instantwin.slotmachine.utilities.SlotSymbols;

public record SlotGameView(long id, long userId, boolean won, BigDecimal amount, List<SlotSymbols> slotStates) {
    
    public static SlotGameView of(SlotGameEntity entity) {
        return new SlotGameView(entity.getId(), entity.getUserId(), entity.isWon(), entity.getAmount(), entity.getSlotStates());
    }

    public long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public boolean getWon() {
        return won;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public List<SlotSymbols> getSlotStates() {
        return slotStates;
    }
}
