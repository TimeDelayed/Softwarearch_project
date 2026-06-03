package com.instantwin.bank.contract.Model.User;

import java.math.BigDecimal;

import com.instantwin.bank.Utilities.InsufficientBalanceException;

public interface IUserEntity {

    Long getId();

    String getFirstName();

    String getLastName();

    BigDecimal getBalance();

    void deposit(BigDecimal amount);

    void withdraw(BigDecimal amount)
            throws InsufficientBalanceException;

    void changeFirstName(String firstName);

    void changeLastName(String lastName);
}
