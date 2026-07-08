package com.instantwin.roulette.Model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.instantwin.roulette.game.BetType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "games")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GameEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private BigDecimal betAmount;

    @Column(nullable = false)
    private int betNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BetType betType;

    @Column(nullable = false)
    private int winningNumber;

    @Column(nullable = false)
    private BigDecimal payout;

    @Column(nullable = false)
    private LocalDateTime playedAt;

    public GameEntity(BigDecimal betAmount, int betNumber, BetType betType, int winningNumber, BigDecimal payout) {
        this.betAmount = betAmount;
        this.betNumber = betNumber;
        this.betType = betType;
        this.winningNumber = winningNumber;
        this.payout = payout;
        this.playedAt = LocalDateTime.now();
    }
}
