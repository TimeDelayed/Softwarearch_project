package com.instantwin.roulette.View;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.instantwin.roulette.contract.view.IUserStatsView;

public class UserStatsView implements IUserStatsView {

    private final long client;
    private final long totalGamesCount;
    private final BigDecimal totalWinnings;
    private final BigDecimal totalLosses;
    private final BigDecimal totalClientProfit;
    private final BigDecimal totalHouseTurnoverFromClient;
    private final BigDecimal totalHouseProfitFromClient;

    private UserStatsView(long client, long totalGamesCount, BigDecimal totalWinnings,
                          BigDecimal totalLosses, BigDecimal totalClientProfit,
                          BigDecimal totalHouseTurnoverFromClient, BigDecimal totalHouseProfitFromClient) {
        this.client = client;
        this.totalGamesCount = totalGamesCount;
        this.totalWinnings = totalWinnings;
        this.totalLosses = totalLosses;
        this.totalClientProfit = totalClientProfit;
        this.totalHouseTurnoverFromClient = totalHouseTurnoverFromClient;
        this.totalHouseProfitFromClient = totalHouseProfitFromClient;
    }

    public static UserStatsView of(long client, long totalGamesCount, BigDecimal totalWinnings,
                                   BigDecimal totalLosses, BigDecimal totalClientProfit,
                                   BigDecimal totalHouseTurnoverFromClient, BigDecimal totalHouseProfitFromClient) {
        return new UserStatsView(client, totalGamesCount, totalWinnings, totalLosses,
                totalClientProfit, totalHouseTurnoverFromClient, totalHouseProfitFromClient);
    }

    @Override
    @JsonProperty("client")
    public long getClient() { return client; }

    @Override
    @JsonProperty("totalGamesCount")
    public long getTotalGamesCount() { return totalGamesCount; }

    @Override
    @JsonProperty("totalWinnings")
    public BigDecimal getTotalWinnings() { return totalWinnings; }

    @Override
    @JsonProperty("totalLosses")
    public BigDecimal getTotalLosses() { return totalLosses; }

    @Override
    @JsonProperty("totalClientProfit")
    public BigDecimal getTotalClientProfit() { return totalClientProfit; }

    @Override
    @JsonProperty("totalHouseTurnoverFromClient")
    public BigDecimal getTotalHouseTurnoverFromClient() { return totalHouseTurnoverFromClient; }

    @Override
    @JsonProperty("totalHouseProfitFromClient")
    public BigDecimal getTotalHouseProfitFromClient() { return totalHouseProfitFromClient; }
}
