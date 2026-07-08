package com.instantwin.slotmachine.view;

import java.math.BigDecimal;

public record SlotHouseStatsView(long totalClientCount, long totalGamesCount, BigDecimal totalProfit,
        BigDecimal totalCashout, BigDecimal totalTurnover) {
            
    public long getTotalClientCount() {
        return totalClientCount;
    }

    public long getTotalGamesCount() {
        return totalGamesCount;
    }

    public BigDecimal getTotalProfit() {
        return totalProfit;
    }

    public BigDecimal getTotalCashout() {
        return totalCashout;
    }

    public BigDecimal getTotalTurnover() {
        return totalTurnover;
    }

}
