package com.instantwin.bank.utilities.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.instantwin.bank.DTO.User.UserTransactionDTO;

public class UserBalanceCalculator {

    public static BigDecimal calculateBalanceForUser(Optional<List<UserTransactionDTO>> transactions) {
        if (transactions.isEmpty()) {
            return BigDecimal.ZERO;
        }
        BigDecimal balance = transactions.get().stream()
                .map(UserTransactionDTO::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return balance;
    }
}
