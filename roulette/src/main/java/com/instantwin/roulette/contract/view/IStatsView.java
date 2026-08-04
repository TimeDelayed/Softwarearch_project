package com.instantwin.roulette.contract.view;

import java.math.BigDecimal;

public interface IStatsView {
    long getTotalClientCount();
    long getTotalGamesCount();
    BigDecimal getTotalProfit();
    BigDecimal getTotalCashOut();
    BigDecimal getTotalTurnover();
}
