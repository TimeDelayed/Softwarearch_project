package com.instantwin.roulette.client.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BankTransactionResponse(long id, long userId, BigDecimal amount) {}
