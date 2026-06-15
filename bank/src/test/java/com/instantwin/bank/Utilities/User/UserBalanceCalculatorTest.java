package com.instantwin.bank.Utilities.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;


import com.instantwin.bank.DTO.User.UserTransactionDTO;

public class UserBalanceCalculatorTest {

    @Test
    void testCalculateBalanceForUser_returns_zero_when_no_transactions_exist() {
        Optional<List<UserTransactionDTO>> transactions = Optional.empty();

        BigDecimal result = UserBalanceCalculator.calculateBalanceForUser(transactions);

        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void testCalculateBalanceForUser_sums_all_transaction_amounts() {
        List<UserTransactionDTO> transactions = List.of(
                new UserTransactionDTO(BigDecimal.valueOf(10)),
                new UserTransactionDTO(BigDecimal.valueOf(-3)),
                new UserTransactionDTO(BigDecimal.valueOf(5)));

        BigDecimal result = UserBalanceCalculator.calculateBalanceForUser(Optional.of(transactions));

        assertEquals(BigDecimal.valueOf(12), result);
    }

    @Test
    void testCalculateBalanceForUser_returns_zero_when_transaction_list_is_empty() {
        BigDecimal result = UserBalanceCalculator.calculateBalanceForUser(Optional.of(List.of()));

        assertEquals(BigDecimal.ZERO, result);
    }
}
