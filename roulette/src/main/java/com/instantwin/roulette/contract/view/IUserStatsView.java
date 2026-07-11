package com.instantwin.roulette.contract.view;

import java.math.BigDecimal;

public interface IUserStatsView {
    long getClient();
    long getTotalGamesCount();
    BigDecimal getTotalWinnings();
    BigDecimal getTotalLosses();
    BigDecimal getTotalClientProfit();
    BigDecimal getTotalHouseTurnoverFromClient();
    BigDecimal getTotalHouseProfitFromClient();
}
