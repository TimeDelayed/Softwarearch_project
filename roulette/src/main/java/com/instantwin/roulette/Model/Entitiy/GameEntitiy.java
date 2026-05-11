package com.instantwin.roulette.Model.Entitiy;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "games")
public class GameEntitiy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user", nullable = false)
    private String user;

    @Column(name = "winnig", nullable = false)
    private Boolean winnig;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "ballPosition", nullable = false)
    private int ballPosition;

    protected GameEntitiy() {
    }

}
