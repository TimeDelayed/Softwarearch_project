package com.instantwin.roulette.contract.client;

import java.math.BigDecimal;
import java.util.Optional;

import com.instantwin.roulette.client.dto.BankTransactionResponse;

public interface IBankClient {
    boolean userExists(long userId);
    Optional<BankTransactionResponse> createTransaction(long userId, BigDecimal amount);
}
