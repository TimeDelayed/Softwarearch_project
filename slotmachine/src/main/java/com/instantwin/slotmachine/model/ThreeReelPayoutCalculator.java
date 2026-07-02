package com.instantwin.slotmachine.model;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.instantwin.slotmachine.contract.model.ISlotConfiguration;
import com.instantwin.slotmachine.contract.model.IThreeReelPayoutCalculator;
import com.instantwin.slotmachine.dto.ThreeReelSpinDTO;
import com.instantwin.slotmachine.utilities.SlotBetMultipliers;
import com.instantwin.slotmachine.utilities.SlotSymbols;

@Component
public class ThreeReelPayoutCalculator implements IThreeReelPayoutCalculator {

    private final ISlotConfiguration slotConfiguration;

    public ThreeReelPayoutCalculator(ISlotConfiguration slotConfiguration) {
        this.slotConfiguration = slotConfiguration;
    }

    @Override
    public BigDecimal calculateMultiplier(ThreeReelSpinDTO spinResult) {

        SlotSymbols first = spinResult.getFirst();
        SlotSymbols second = spinResult.getSecond();
        SlotSymbols third = spinResult.getThird();

        SlotBetMultipliers multiplier = SlotBetMultipliers.NO_MULTIPLIER;

        if (hasAnyMatchingPair(first, second, third)) {
            multiplier = applyTripleOrDoubleHit(first, second, third, multiplier);
        }

        multiplier = applySingleDiamondIfPresent(first, second, third, multiplier);

        return slotConfiguration.getSymbolMultipliers().get(multiplier);
    }

    private boolean hasAnyMatchingPair(SlotSymbols first, SlotSymbols second, SlotSymbols third) {
        return first == second || first == third || second == third;
    }

    private SlotBetMultipliers applyTripleOrDoubleHit(
            SlotSymbols first,
            SlotSymbols second,
            SlotSymbols third,
            SlotBetMultipliers currentMultiplier) {

        if (first == second && second == third) {
            return maxMultiplier(
                    currentMultiplier,
                    slotConfiguration.getTripleHitReferenceMap().get(first));
        }

        if (first == second || first == third) {
            return maxMultiplier(
                    currentMultiplier,
                    slotConfiguration.getDoubleHitReferenceMap().get(first));
        }

        return maxMultiplier(
                currentMultiplier,
                slotConfiguration.getDoubleHitReferenceMap().get(second));
    }

    private SlotBetMultipliers applySingleDiamondIfPresent(
            SlotSymbols first,
            SlotSymbols second,
            SlotSymbols third,
            SlotBetMultipliers currentMultiplier) {

        if (first == SlotSymbols.DIAMOND
                || second == SlotSymbols.DIAMOND
                || third == SlotSymbols.DIAMOND) {
            return maxMultiplier(currentMultiplier, SlotBetMultipliers.SINGLE_DIAMOND);
        }

        return currentMultiplier;
    }

    private SlotBetMultipliers maxMultiplier(
            SlotBetMultipliers currentMultiplier,
            SlotBetMultipliers candidateMultiplier) {

        BigDecimal currentValue = slotConfiguration.getSymbolMultipliers().get(currentMultiplier);
        BigDecimal candidateValue = slotConfiguration.getSymbolMultipliers().get(candidateMultiplier);

        return currentValue.compareTo(candidateValue) < 0
                ? candidateMultiplier
                : currentMultiplier;
    }
}