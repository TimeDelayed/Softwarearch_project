package com.instantwin.slotmachine.view;

import java.math.BigDecimal;
import java.util.List;

import com.instantwin.slotmachine.model.SlotGameEntity;
import com.instantwin.slotmachine.utilities.SlotSymbols;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Public representation of a settled slot game.")
public record SlotGameView(
        @Schema(description = "ID of the stored game", example = "1") long id,
        @Schema(description = "ID of the user who played the game", example = "1") long userId,
        @Schema(description = "Amount wagered by the user", example = "10.00") BigDecimal betAmount,
        @Schema(description = "Whether a payout combination was hit. This does not necessarily mean a positive net result.", example = "true") boolean won,
        @Schema(description = "Net result of the game: gross payout minus bet amount", example = "5.00") BigDecimal amount,
        @ArraySchema(schema = @Schema(implementation = SlotSymbols.class),
                arraySchema = @Schema(description = "Symbols displayed on the three reels")) List<SlotSymbols> slotStates) {
    
    public static SlotGameView of(SlotGameEntity entity) {
        return new SlotGameView(entity.getId(), entity.getUserId(), entity.getBetAmount(), entity.isWon(), entity.getAmount(), entity.getSlotStates());
    }

    public long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public BigDecimal getBetAmount() {
        return betAmount;
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
