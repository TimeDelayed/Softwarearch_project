package com.instantwin.roulette.utilities;

public class BankTransactionFailedException extends RuntimeException {

    public BankTransactionFailedException(String message) {
        super(message);
    }
}
