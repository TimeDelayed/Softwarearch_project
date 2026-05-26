package com.instantwin.bank.View.User;

import java.math.BigDecimal;

import com.instantwin.bank.Model.User.UserEntity;
import com.instantwin.bank.contract.View.User.IUserView;

public record UserView(
        String firstName,
        String lastName,
        long id,
        BigDecimal balance) implements IUserView {

    public static IUserView of(UserEntity user) {
        return new UserView(user.getFirstName(), user.getLastName(), user.getId(), user.getBalance());
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
