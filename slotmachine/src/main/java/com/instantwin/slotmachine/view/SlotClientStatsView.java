package com.instantwin.slotmachine.view;

import java.math.BigDecimal;

public record SlotClientStatsView(long userId, long totalGamesCount, BigDecimal totalWinnings, BigDecimal totalLosses,
        BigDecimal totalClientProfit, BigDecimal totalHouseTurnoverFromClient, BigDecimal totalHouseProfitFromClient) {

    public long getUserId() {
        return userId;
    }

    public long getTotalGamesCount() {
        return totalGamesCount;
    }

    public BigDecimal getTotalWinnings() {
        return totalWinnings;
    }

    public BigDecimal getTotalLosses() {
        return totalLosses;
    }

    public BigDecimal getTotalClientProfit() {
        return totalClientProfit;
    }

    public BigDecimal getTotalHouseTurnoverFromClient() {
        return totalHouseTurnoverFromClient;
    }

    public BigDecimal getTotalHouseProfitFromClient() {
        return totalHouseProfitFromClient;
    }

}
