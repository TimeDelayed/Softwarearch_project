package com.instantwin.bank.utilities.User;

public abstract class UserErrorMessages {
    public static final String USER_NAME_IS_INVALID = "Invalid or empty name!";

    public static final String CURRENCY_INPUT_INVALID = "Invalid or empty amount! Must be positive";

    public static final String NEGATIVE_BALANCE_ERROR = "Balance cannot be negative!";

    public static final String INIT_WITH_NULL_ERROR = "Can't initiate this class with null parameter!";

    public static final String DECIMAL_INPUT_INVALID = "Invalid decimal input! Must be between 0 and 99!";

    public static final String AMOUNT_INPUT_INVALID = "Invalid amount input! Must be positive!";

    public static final String TRANSACTION_REQUEST_FAILED = "Transaction could not be created!";
}
