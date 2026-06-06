package com.instantwin.bank.View.User;

import java.math.BigDecimal;

import com.instantwin.bank.contract.Model.User.IUserEntity;
import com.instantwin.bank.contract.View.User.IUserDeleteView;

public record UserDeleteView(
                String firstName,
                String lastName,
                BigDecimal balance) implements IUserDeleteView {

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
