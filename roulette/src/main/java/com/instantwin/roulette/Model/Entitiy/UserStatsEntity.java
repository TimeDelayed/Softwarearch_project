package com.instantwin.roulette.Model.Entitiy;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "userStats")
public class UserStatsEntity {

    @Id
    @Column(name = "userId", unique = true, nullable = false)
    private Long id;

    @Column(name = "totalGamesCount", nullable = false)
    private int totalGamesCount;

    @Column(name = "totalWinnings", nullable = false)
    private int totalWinnings;

    @Column(name = "totalLosses", nullable = false)
    private int totalLosses;

    @Column(name = "totalClientProfit", nullable = false)
    private int totalClientProfit;

    @Column(name = "totalHouseTurnoverFromClient", nullable = false)
    private int totalHouseTurnoverFromClient;

    @Column(name = "totalHousProfitFromClient", nullable = false)
    private int totalHousProfitFromClient;

    protected UserStatsEntity() {
    }


}
