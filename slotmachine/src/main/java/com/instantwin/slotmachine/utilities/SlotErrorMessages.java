package com.instantwin.slotmachine.utilities;

public class SlotErrorMessages {
    public static final String BET_AMOUNT_NEGATIVE = "Invalid betting amount, can't be negative (Must be > 0)!";
    public static final String BET_AMOUNT_NULL = "Invalid betting amount, can't be null!";

    public static final String INVALID_SLOT_PROBABILITIES = "Invalid slot reel probabilities, must be between 0 and 1! Must accumulate to 1.0!";

    public static final String INVALID_THREE_REEL_CONFIGURATION = "Invalid slot configuration, must have exactly 3 reels!";

    public static final String USER_ID_NULL = "Invalid user ID, cant be null!";
    public static final String USER_ID_NEGATIVE = "Invalid user ID, cant be negative!";
    public static final String INVALID_AMOUNT_NULL = "Invalid amount, cant be null!";
    public static final String INVALID_AMOUNT_NEGATIVE = "Invalid amount, cant be < 0!";
    public static final String INVALID_SLOT_STATES_NULL = "Invalid slot states, cant be null!";

    public static final String GAME_RULES_FILE_ERROR = "Game rules file could not be loaded";
    public static final String GAME_CHANCES_FILE_ERROR = "Game info file could not be loaded";
    public static final String GAME_RULES_FILE_NOT_FOUND = "Game rules file not found at path: ";
    public static final String GAME_CHANCES_FILE_NOT_FOUND = "Game info file not found at path: ";
}
