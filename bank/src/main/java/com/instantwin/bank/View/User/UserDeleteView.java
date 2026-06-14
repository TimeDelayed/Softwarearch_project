package com.instantwin.bank.View.User;

import java.math.BigDecimal;

import com.instantwin.bank.contract.Model.User.IUserEntity;
import com.instantwin.bank.contract.View.User.IUserDeleteView;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserDeleteView(
                @NotNull @NotBlank String firstName,
                @NotNull @NotBlank String lastName,
                @NotNull BigDecimal balance) implements IUserDeleteView {

        public static IUserDeleteView of(IUserEntity user, BigDecimal balance) {
                return new UserDeleteView(user.getFirstName(), user.getLastName(), balance);
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
        public BigDecimal getBalance() {
                return balance;
        }

}
