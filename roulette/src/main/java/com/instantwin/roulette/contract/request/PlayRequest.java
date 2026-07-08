package com.instantwin.roulette.contract.request;

import java.math.BigDecimal;

import com.instantwin.roulette.game.BetType;

public record PlayRequest(BigDecimal betAmount, int betNumber, BetType betType) {}
