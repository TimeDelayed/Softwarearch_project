package com.instantwin.roulette.client.dto;

import java.math.BigDecimal;

public record BankTransactionRequest(BigDecimal amount, String invoicingParty) {}
