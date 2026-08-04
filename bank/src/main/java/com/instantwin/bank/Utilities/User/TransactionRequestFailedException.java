package com.instantwin.bank.utilities.User;

public class TransactionRequestFailedException extends RuntimeException {

    public TransactionRequestFailedException(String message) {
        super(message);
    }
}
