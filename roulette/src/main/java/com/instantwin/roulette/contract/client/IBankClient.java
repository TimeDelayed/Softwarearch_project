package com.instantwin.roulette.contract.client;

import java.math.BigDecimal;

import org.springframework.http.ResponseEntity;

public interface IBankClient {
    ResponseEntity<String> requestTransaction(long userId, BigDecimal netAmount);
}
