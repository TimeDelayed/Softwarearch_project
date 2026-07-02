package com.instantwin.slotmachine.utilities;

public class SlotErrorMessages {
    public static final String BET_AMOUNT_NEGATIVE = "Invalid betting amount, can't be negative (Must be > 0)!";
    public static final String BET_AMOUNT_NULL = "Invalid betting amount, can't be null!";
    
    public static final String INVALID_SLOT_PROBABILITIES = "Invalid slot reel probabilities, must be between 0 and 1! Must accumulate to 1.0!";

    public static final String INVALID_THREE_REEL_CONFIGURATION = "Invalid slot configuration, must have exactly 3 reels!";
}
