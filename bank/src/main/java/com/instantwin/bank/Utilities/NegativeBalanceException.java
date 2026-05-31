package com.instantwin.bank.Utilities;

public class NegativeBalanceException extends Exception {
    public NegativeBalanceException() {
        super(ErrorMessages.NEGATIVE_BALANCE_ERROR);
    }
    
}
