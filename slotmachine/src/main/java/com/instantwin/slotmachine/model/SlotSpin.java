package com.instantwin.slotmachine.model;

import java.util.HashMap;
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
            put(SlotSymbols.LEMON, 0.21f);
            put(SlotSymbols.DIAMOND, 0.29f);
            put(SlotSymbols.BELL, 0.02f);
        }
    };

    private final HashMap<SlotSymbols, SlotBetMultipliers> doubleHitReferenceMap = new HashMap<>() {
        {
            put(SlotSymbols.CHERRY, SlotBetMultipliers.DOUBLE_CHERRY);
            put(SlotSymbols.LEMON, SlotBetMultipliers.DOUBLE_LEMON);
            put(SlotSymbols.BELL, SlotBetMultipliers.DOUBLE_BELL);
            put(SlotSymbols.DIAMOND, SlotBetMultipliers.DOUBLE_DIAMOND);
        }
    };

    private final HashMap<SlotSymbols, SlotBetMultipliers> tripleHitReferenceMap = new HashMap<>() {
        {
            put(SlotSymbols.CHERRY, SlotBetMultipliers.TRIPLE_CHERRY);
            put(SlotSymbols.LEMON, SlotBetMultipliers.TRIPLE_LEMON);
            put(SlotSymbols.BELL, SlotBetMultipliers.TRIPLE_BELL);
            put(SlotSymbols.DIAMOND, SlotBetMultipliers.TRIPLE_DIAMOND);
        }
    };
    
    private final HashMap<SlotBetMultipliers, Float> symbolMultipliers = new HashMap<>() {
        {
            put(SlotBetMultipliers.NO_MULTIPLIER, 0f);
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

    private final SplittableRandom rng;

    private SlotSymbols[] latestSpin;

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

    @Override
    public Float getBetMultiplier() {
        SlotSymbols first = this.latestSpin[0];
        SlotSymbols second = this.latestSpin[1];
        SlotSymbols third = this.latestSpin[2];

        SlotBetMultipliers multiplier = SlotBetMultipliers.NO_MULTIPLIER;

        if (hasAnyMatchingPair(first, second, third)) {
            multiplier = applyTripleOrDoubleHit(first, second, third, multiplier);
        }

        multiplier = applySingleDiamondIfPresent(first, second, third, multiplier);

        return symbolMultipliers.get(multiplier);
    }

    private boolean hasAnyMatchingPair(SlotSymbols first, SlotSymbols second, SlotSymbols third) {
        return first == second || first == third || second == third;
    }

    private SlotBetMultipliers applyTripleOrDoubleHit(
            SlotSymbols first,
            SlotSymbols second,
            SlotSymbols third,
            SlotBetMultipliers currentMultiplier
    ) {
        if (first == second && second == third) {
            return maxMultiplier(currentMultiplier, tripleHitReferenceMap.get(first));
        }

        if (first == second || first == third) {
            return maxMultiplier(currentMultiplier, doubleHitReferenceMap.get(first));
        }

        return maxMultiplier(currentMultiplier, doubleHitReferenceMap.get(second));
    }

    private SlotBetMultipliers applySingleDiamondIfPresent(
            SlotSymbols first,
            SlotSymbols second,
            SlotSymbols third,
            SlotBetMultipliers currentMultiplier
    ) {
        if (first == SlotSymbols.DIAMOND
                || second == SlotSymbols.DIAMOND
                || third == SlotSymbols.DIAMOND) {
            return maxMultiplier(currentMultiplier, SlotBetMultipliers.SINGLE_DIAMOND);
        }

        return currentMultiplier;
    }

    private SlotBetMultipliers maxMultiplier(
            SlotBetMultipliers currentMultiplier,
            SlotBetMultipliers candidateMultiplier
    ) {
        return symbolMultipliers.get(currentMultiplier) < symbolMultipliers.get(candidateMultiplier)
                ? candidateMultiplier
                : currentMultiplier;
    }

    @Override
    public SlotSymbols[] spin() {
        int numReels = 3;
        SlotSymbols[] result = new SlotSymbols[numReels];
        for (int i = 0; i < numReels; i++) {
            float spin = rng.nextFloat();
            result[i] = getSymbolForSpin(spin);
        }
        this.latestSpin = result;
        return result;
    }

}
