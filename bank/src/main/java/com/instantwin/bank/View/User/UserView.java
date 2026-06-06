package com.instantwin.bank.View.User;

import java.math.BigDecimal;

import com.instantwin.bank.contract.Model.User.IUserEntity;
import com.instantwin.bank.contract.View.User.IUserView;

public record UserView(
        String firstName,
        String lastName,
        long id,
        BigDecimal balance) implements IUserView {

    public static IUserView of(IUserEntity user, BigDecimal balance) {
        return new UserView(user.getFirstName(), user.getLastName(), user.getId(), balance);
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public BigDecimal getBalance() {
        return balance;
    }
}
