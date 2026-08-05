package com.instantwin.slotmachine.view;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Aggregated house statistics across all slot games.")
public record SlotHouseStatsView(
        @Schema(description = "Number of distinct users who played slots", example = "4") long totalClientCount,
        @Schema(description = "Total number of stored slot games", example = "25") long totalGamesCount,
        @Schema(description = "Net profit of the house", example = "30.00") BigDecimal totalProfit,
        @Schema(description = "Total gross payout returned to users", example = "220.00") BigDecimal totalCashout,
        @Schema(description = "Sum of all slot bets", example = "250.00") BigDecimal totalTurnover) {
            
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
