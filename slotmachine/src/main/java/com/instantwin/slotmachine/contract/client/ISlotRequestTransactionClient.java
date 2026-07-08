package com.instantwin.slotmachine.contract.client;

import java.math.BigDecimal;

import org.springframework.http.ResponseEntity;

public interface ISlotRequestTransactionClient {
    ResponseEntity<String> requestTransaction(long userId, BigDecimal amount);
}
