package com.instantwin.roulette.utilities;

public class RouletteErrorMessages {

    public static final String BET_AMOUNT_INVALID = "Bet amount must be greater than zero!";
    public static final String BET_TYPE_INVALID = "Bet type must not be null!";
    public static final String BET_NUMBER_INVALID = "Bet number is invalid for the selected bet type!";
    public static final String USER_ID_INVALID = "User ID must be greater than zero!";
    public static final String WINNING_NUMBER_INVALID = "Winning number must be between 0 and 36!";
    public static final String PAYOUT_INVALID = "Payout must not be null or negative!";
    public static final String GAME_RESULT_INVALID = "Payout does not match the recorded roulette result!";
    public static final String BANK_TRANSACTION_FAILED = "Bank transaction failed!";

    private RouletteErrorMessages() {
    }
}
