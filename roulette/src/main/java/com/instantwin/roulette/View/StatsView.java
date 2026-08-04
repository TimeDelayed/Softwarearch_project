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
    @JsonProperty("total_client_count")
    public long getTotalClientCount() { return totalClientCount; }

    @Override
    @JsonProperty("total_games_count")
    public long getTotalGamesCount() { return totalGamesCount; }

    @Override
    @JsonProperty("total_profit")
    public BigDecimal getTotalProfit() { return totalProfit; }

    @Override
    @JsonProperty("total_cash_out")
    public BigDecimal getTotalCashOut() { return totalCashOut; }

    @Override
    @JsonProperty("total_turnover")
    public BigDecimal getTotalTurnover() { return totalTurnover; }
}
