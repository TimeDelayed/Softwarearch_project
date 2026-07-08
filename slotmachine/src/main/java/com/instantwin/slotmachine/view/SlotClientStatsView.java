package com.instantwin.slotmachine.view;

import java.math.BigDecimal;

public record SlotClientStatsView(long userId, long totalGamesCount, long totalLosses, long totalWins,
        BigDecimal totalClientProfit, BigDecimal totalHouseTurnoverFromClient, BigDecimal totalHouseProfitFromClient) {

    public long getUserId() {
        return userId;
    }

    public long getTotalGamesCount() {
        return totalGamesCount;
    }

    public long getTotalLosses() {
        return totalLosses;
    }

    public long getTotalWins() {
        return totalWins;
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
