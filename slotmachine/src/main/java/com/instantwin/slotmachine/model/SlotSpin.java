package com.instantwin.slotmachine.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.SplittableRandom;

import org.springframework.stereotype.Component;

import com.instantwin.slotmachine.contract.model.ISlotSpin;
import com.instantwin.slotmachine.utilities.InvalidSlotProbabilities;
import com.instantwin.slotmachine.utilities.SlotBetMultipliers;
import com.instantwin.slotmachine.utilities.SlotErrorMessages;
import com.instantwin.slotmachine.utilities.SlotSymbols;

@Component
public class SlotSpin implements ISlotSpin {

    private final HashMap<SlotSymbols, Float> reelProbabilities = new HashMap<>() {
        {
            put(SlotSymbols.CHERRY, 0.48f);
            put(SlotSymbols.LEMON, 0.29f);
            put(SlotSymbols.BELL, 0.21f);
            put(SlotSymbols.DIAMOND, 0.2f);
        }
    };

    private final HashMap<SlotBetMultipliers, Float> symbolMultipliers = new HashMap<>() {
        {
            put(SlotBetMultipliers.DOUBLE_CHERRY, 0.80f);
            put(SlotBetMultipliers.TRIPLE_CHERRY, 0.99f);
            put(SlotBetMultipliers.DOUBLE_LEMON, 1.15f);
            put(SlotBetMultipliers.TRIPLE_LEMON, 2.5f);
            put(SlotBetMultipliers.DOUBLE_BELL, 1.7f);
            put(SlotBetMultipliers.TRIPLE_BELL, 5.75f);
            put(SlotBetMultipliers.SINGLE_DIAMOND, 1.5f);
            put(SlotBetMultipliers.DOUBLE_DIAMOND, 15.8f);
            put(SlotBetMultipliers.TRIPLE_DIAMOND, 498.0f);

        }
    };

    private final int numReels = 3;

    private final SplittableRandom rng;

    public SlotSpin(int seed) {
        this.rng = new SplittableRandom(seed);
    }

    public SlotSpin() {
        this.rng = new SplittableRandom();
    }

    private SlotSymbols getSymbolForSpin(Float spin) {
        float cumulativeProbability = 0.0f;
        for (Entry<SlotSymbols, Float> entry : reelProbabilities.entrySet()) {
            SlotSymbols symbol = entry.getKey();
            cumulativeProbability += entry.getValue();
            if (spin <= cumulativeProbability) {
                return symbol;
            }
        }
        throw new InvalidSlotProbabilities(SlotErrorMessages.INVALID_SLOT_PROBABILITIES);
    }

    private SlotBetMultipliers getMultiplierForSymbols(SlotSymbols[] symbols) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public SlotSymbols[] spin() {
        SlotSymbols[] result = new SlotSymbols[numReels];
        for (int i = 0; i < numReels; i++) {
            float spin = rng.nextFloat();
            result[i] = getSymbolForSpin(spin);
        }
    }

}
