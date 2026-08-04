package com.instantwin.slotmachine.model;

import java.util.SplittableRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.instantwin.slotmachine.contract.model.ISlotProbabilityConfiguration;
import com.instantwin.slotmachine.contract.model.ISlotSpinner;
import com.instantwin.slotmachine.dto.ThreeReelSpinDTO;
import com.instantwin.slotmachine.utilities.InvalidSlotProbabilities;
import com.instantwin.slotmachine.utilities.SlotErrorMessages;
import com.instantwin.slotmachine.utilities.SlotSymbols;

@Component
public class SlotSpinner implements ISlotSpinner {

    private static final float TOTAL_PROBABILITY = 1.0f;
    private static final float PROBABILITY_TOLERANCE = 0.0001f;

    private final ISlotProbabilityConfiguration slotConfiguration;
    private final SplittableRandom rng;
    private final double configuredTotalProbability;
    
    @Autowired
    public SlotSpinner(ISlotProbabilityConfiguration slotConfiguration) {
        this(slotConfiguration, new SplittableRandom());
    }

    public SlotSpinner(ISlotProbabilityConfiguration slotConfiguration, SplittableRandom rng) {
        this.slotConfiguration = slotConfiguration;
        this.rng = rng;
        this.configuredTotalProbability = validateProbabilities();
    }

    @Override
    public ThreeReelSpinDTO spin() {
        return new ThreeReelSpinDTO(
                getSymbolForSpin(rng.nextFloat()),
                getSymbolForSpin(rng.nextFloat()),
                getSymbolForSpin(rng.nextFloat()));
    }

    private SlotSymbols getSymbolForSpin(float spin) {
        double cumulativeProbability = 0.0;
        double normalizedSpin = spin * configuredTotalProbability;

        for (var entry : slotConfiguration.getProbabilities().entrySet()) {
            cumulativeProbability += entry.getValue();

            if (normalizedSpin < cumulativeProbability) {
                return entry.getKey();
            }
        }

        throw new InvalidSlotProbabilities(SlotErrorMessages.INVALID_SLOT_PROBABILITIES);
    }

    private double validateProbabilities() {
        var probabilities = slotConfiguration.getProbabilities();
        double totalProbability = 0.0;

        if (probabilities == null || probabilities.size() != SlotSymbols.values().length) {
            throw new InvalidSlotProbabilities(SlotErrorMessages.INVALID_SLOT_PROBABILITIES);
        }

        for (var symbol : SlotSymbols.values()) {
            var probability = probabilities.get(symbol);

            if (probability == null
                    || !Float.isFinite(probability)
                    || probability < 0.0f
                    || probability > 1.0f) {
                throw new InvalidSlotProbabilities(SlotErrorMessages.INVALID_SLOT_PROBABILITIES);
            }

            totalProbability += probability;
        }

        if (Math.abs(totalProbability - TOTAL_PROBABILITY) > PROBABILITY_TOLERANCE) {
            throw new InvalidSlotProbabilities(SlotErrorMessages.INVALID_SLOT_PROBABILITIES);
        }

        return totalProbability;
    }
}
