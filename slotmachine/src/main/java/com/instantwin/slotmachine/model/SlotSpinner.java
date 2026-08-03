package com.instantwin.slotmachine.model;

import java.util.SplittableRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.instantwin.slotmachine.contract.model.ISlotConfiguration;
import com.instantwin.slotmachine.contract.model.ISlotSpinner;
import com.instantwin.slotmachine.dto.ThreeReelSpinDTO;
import com.instantwin.slotmachine.utilities.InvalidSlotProbabilities;
import com.instantwin.slotmachine.utilities.SlotErrorMessages;
import com.instantwin.slotmachine.utilities.SlotSymbols;

@Component
public class SlotSpinner implements ISlotSpinner {

    private final ISlotConfiguration slotConfiguration;
    private final SplittableRandom rng;

    // TODO VERBESSERN FÜR README
    @Autowired
    public SlotSpinner(ISlotConfiguration slotConfiguration) {
        this(slotConfiguration, new SplittableRandom());
    }

    public SlotSpinner(ISlotConfiguration slotConfiguration, SplittableRandom rng) {
        this.slotConfiguration = slotConfiguration;
        this.rng = rng;
    }

    @Override
    public ThreeReelSpinDTO spin() {
        return new ThreeReelSpinDTO(
                getSymbolForSpin(rng.nextFloat()),
                getSymbolForSpin(rng.nextFloat()),
                getSymbolForSpin(rng.nextFloat()));
    }

    private SlotSymbols getSymbolForSpin(float spin) {
        float cumulativeProbability = 0.0f;

        for (var entry : slotConfiguration.getProbabilities().entrySet()) {
            cumulativeProbability += entry.getValue();

            if (spin <= cumulativeProbability) {
                return entry.getKey();
            }
        }

        throw new InvalidSlotProbabilities(SlotErrorMessages.INVALID_SLOT_PROBABILITIES);
    }
}