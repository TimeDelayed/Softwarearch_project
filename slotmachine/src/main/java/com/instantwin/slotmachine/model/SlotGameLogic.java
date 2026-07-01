package com.instantwin.slotmachine.model;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.instantwin.slotmachine.contract.model.ISlotGameLogic;
import com.instantwin.slotmachine.contract.model.ISlotSpin;
import com.instantwin.slotmachine.dto.SlotGameResultDTO;
import com.instantwin.slotmachine.utilities.BetAmountInvalidException;
import com.instantwin.slotmachine.utilities.SlotErrorMessages;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Component
@Getter
@RequiredArgsConstructor()
public class SlotGameLogic implements ISlotGameLogic{

    private final ISlotSpin slotSpin;
    
    void validateBetAmount(BigDecimal betAmount) {
        if (betAmount == null) {
            throw new BetAmountInvalidException(SlotErrorMessages.BET_AMOUNT_NULL);
        }
        if (betAmount.signum() <= 0) {
            throw new BetAmountInvalidException(SlotErrorMessages.BET_AMOUNT_NEGATIVE);
        }
    }

    @Override
    public SlotGameResultDTO placeBet(BigDecimal betAmount) {
        validateBetAmount(betAmount);
        throw new UnsupportedOperationException("Unimplemented!");
    }
    
}
