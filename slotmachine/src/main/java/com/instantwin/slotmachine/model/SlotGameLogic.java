package com.instantwin.slotmachine.model;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.instantwin.slotmachine.contract.model.ISlotGameLogic;
import com.instantwin.slotmachine.contract.model.IThreeReelPayoutCalculator;
import com.instantwin.slotmachine.contract.model.ISlotSpinner;
import com.instantwin.slotmachine.dto.ThreeReelSpinDTO;
import com.instantwin.slotmachine.utilities.BetAmountInvalidException;
import com.instantwin.slotmachine.utilities.SlotErrorMessages;
import com.instantwin.slotmachine.view.SlotGameResultView;

@Component
public class SlotGameLogic implements ISlotGameLogic {

    private final ISlotSpinner slotSpinner;
    private final IThreeReelPayoutCalculator payoutCalculator;

    public SlotGameLogic(ISlotSpinner slotSpinner, IThreeReelPayoutCalculator payoutCalculator) {
        this.slotSpinner = slotSpinner;
        this.payoutCalculator = payoutCalculator;
    }

    @Override
    public SlotGameResultView placeBet(BigDecimal betAmount) {
        validateBetAmount(betAmount);

        ThreeReelSpinDTO spinResult = slotSpinner.spin();
        BigDecimal betMultiplier = payoutCalculator.calculateMultiplier(spinResult);

        boolean won = betMultiplier.compareTo(BigDecimal.ZERO) > 0;
        BigDecimal winnings = betAmount.multiply(betMultiplier);

        return new SlotGameResultView(betAmount, spinResult, won, winnings);
    }

    private void validateBetAmount(BigDecimal betAmount) {
        if (betAmount == null) {
            throw new BetAmountInvalidException(SlotErrorMessages.BET_AMOUNT_NULL);
        }

        if (betAmount.signum() <= 0) {
            throw new BetAmountInvalidException(SlotErrorMessages.BET_AMOUNT_NEGATIVE);
        }
    }
}
