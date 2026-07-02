package com.instantwin.slotmachine.model;

import java.math.BigDecimal;
import java.util.EnumMap;

import org.springframework.stereotype.Component;

import com.instantwin.slotmachine.contract.model.ISlotConfiguration;
import com.instantwin.slotmachine.utilities.SlotBetMultipliers;
import com.instantwin.slotmachine.utilities.SlotSymbols;

import lombok.Getter;

@Component
@Getter
public class SlotConfiguration implements ISlotConfiguration {

    private final EnumMap<SlotSymbols, Float> probabilities = new EnumMap<>(SlotSymbols.class);
    private final EnumMap<SlotSymbols, SlotBetMultipliers> doubleHitReferenceMap = new EnumMap<>(SlotSymbols.class);
    private final EnumMap<SlotSymbols, SlotBetMultipliers> tripleHitReferenceMap = new EnumMap<>(SlotSymbols.class);
    private final EnumMap<SlotBetMultipliers, BigDecimal> symbolMultipliers = new EnumMap<>(SlotBetMultipliers.class);
    private final int numberOfReels = 3;

    public SlotConfiguration() {
        probabilities.put(SlotSymbols.CHERRY, 0.48f);
        probabilities.put(SlotSymbols.LEMON, 0.21f);
        probabilities.put(SlotSymbols.DIAMOND, 0.29f);
        probabilities.put(SlotSymbols.BELL, 0.02f);

        doubleHitReferenceMap.put(SlotSymbols.CHERRY, SlotBetMultipliers.DOUBLE_CHERRY);
        doubleHitReferenceMap.put(SlotSymbols.LEMON, SlotBetMultipliers.DOUBLE_LEMON);
        doubleHitReferenceMap.put(SlotSymbols.BELL, SlotBetMultipliers.DOUBLE_BELL);
        doubleHitReferenceMap.put(SlotSymbols.DIAMOND, SlotBetMultipliers.DOUBLE_DIAMOND);

        tripleHitReferenceMap.put(SlotSymbols.CHERRY, SlotBetMultipliers.TRIPLE_CHERRY);
        tripleHitReferenceMap.put(SlotSymbols.LEMON, SlotBetMultipliers.TRIPLE_LEMON);
        tripleHitReferenceMap.put(SlotSymbols.BELL, SlotBetMultipliers.TRIPLE_BELL);
        tripleHitReferenceMap.put(SlotSymbols.DIAMOND, SlotBetMultipliers.TRIPLE_DIAMOND);

        symbolMultipliers.put(SlotBetMultipliers.NO_MULTIPLIER, new BigDecimal("0"));
        symbolMultipliers.put(SlotBetMultipliers.DOUBLE_CHERRY, new BigDecimal("0.80"));
        symbolMultipliers.put(SlotBetMultipliers.TRIPLE_CHERRY, new BigDecimal("0.99"));
        symbolMultipliers.put(SlotBetMultipliers.DOUBLE_LEMON, new BigDecimal("1.15"));
        symbolMultipliers.put(SlotBetMultipliers.TRIPLE_LEMON, new BigDecimal("2.50"));
        symbolMultipliers.put(SlotBetMultipliers.DOUBLE_BELL, new BigDecimal("1.70"));
        symbolMultipliers.put(SlotBetMultipliers.TRIPLE_BELL, new BigDecimal("5.75"));
        symbolMultipliers.put(SlotBetMultipliers.SINGLE_DIAMOND, new BigDecimal("1.50"));
        symbolMultipliers.put(SlotBetMultipliers.DOUBLE_DIAMOND, new BigDecimal("15.80"));
        symbolMultipliers.put(SlotBetMultipliers.TRIPLE_DIAMOND, new BigDecimal("498.00"));
    }
}