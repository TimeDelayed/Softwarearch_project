package com.instantwin.bank.Utilities;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.instantwin.bank.DTO.User.UserRequestTransaction;

public class UserBalanceCalculator {

    public static BigDecimal calculateBalanceForUser(Optional<List<UserRequestTransaction>> transactions) {
        if (transactions.isEmpty()) {
            return BigDecimal.ZERO;
        }
        BigDecimal balance = transactions.get().stream()
                .map(UserRequestTransaction::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return balance;
    }
}
