package com.instantwin.bank.contract.View.User;

import java.math.BigDecimal;

public interface IUserView {
    String getFirstName();

    String getLastName();

    BigDecimal getBalance();

    long getId();
}
