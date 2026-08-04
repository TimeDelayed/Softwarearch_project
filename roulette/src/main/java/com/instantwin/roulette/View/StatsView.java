package com.instantwin.roulette.View;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.instantwin.roulette.contract.view.IStatsView;

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
    public long getTotalClientCount() { return totalClientCount; }

    @Override
    @JsonProperty("totalGamesCount")
    public long getTotalGamesCount() { return totalGamesCount; }

    @Override
    @JsonProperty("totalProfit")
    public BigDecimal getTotalProfit() { return totalProfit; }

    @Override
    @JsonProperty("totalCashOut")
    public BigDecimal getTotalCashOut() { return totalCashOut; }

    @Override
    @JsonProperty("totalTurnover")
    public BigDecimal getTotalTurnover() { return totalTurnover; }
}
