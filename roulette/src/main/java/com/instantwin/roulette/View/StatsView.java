package com.instantwin.roulette.View;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.instantwin.roulette.contract.view.IStatsView;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Aggregated house statistics across all roulette games.")
public class StatsView implements IStatsView {

    private final long totalClientCount;
    private final long totalGamesCount;
    private final BigDecimal totalProfit;
    private final BigDecimal totalCashOut;
    private final BigDecimal totalTurnover;

    private StatsView(long totalClientCount, long totalGamesCount,
                      BigDecimal totalProfit, BigDecimal totalCashOut, BigDecimal totalTurnover) {
        this.totalClientCount = totalClientCount;
        this.totalGamesCount = totalGamesCount;
        this.totalProfit = totalProfit;
        this.totalCashOut = totalCashOut;
        this.totalTurnover = totalTurnover;
    }

    public static StatsView of(long clientCount, long gamesCount,
                               BigDecimal totalProfit, BigDecimal totalCashOut, BigDecimal totalTurnover) {
        return new StatsView(clientCount, gamesCount, totalProfit, totalCashOut, totalTurnover);
    }

    @Override
    @JsonProperty("totalClientCount")
    @Schema(description = "Number of distinct users who played roulette", example = "4")
    public long getTotalClientCount() { return totalClientCount; }

    @Override
    @JsonProperty("totalGamesCount")
    @Schema(description = "Total number of stored roulette games", example = "25")
    public long getTotalGamesCount() { return totalGamesCount; }

    @Override
    @JsonProperty("totalProfit")
    @Schema(description = "Net profit of the house", example = "30.00")
    public BigDecimal getTotalProfit() { return totalProfit; }

    @Override
    @JsonProperty("totalCashOut")
    @Schema(description = "Total gross payout returned to users", example = "220.00")
    public BigDecimal getTotalCashOut() { return totalCashOut; }

    @Override
    @JsonProperty("totalTurnover")
    @Schema(description = "Sum of all roulette bets", example = "250.00")
    public BigDecimal getTotalTurnover() { return totalTurnover; }
}
