package com.instantwin.bank.View.User;

import java.math.BigDecimal;

import com.instantwin.bank.contract.Model.User.IUserEntity;
import com.instantwin.bank.contract.View.User.IUserView;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.NotBlank;

public record UserView(
        @NotNull @NotBlank String firstName,
        @NotNull @NotBlank String lastName,
        @Positive long id,
        @NotNull BigDecimal balance) implements IUserView {

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
