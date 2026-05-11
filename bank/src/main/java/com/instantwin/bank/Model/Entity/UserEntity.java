package com.instantwin.bank.Model.Entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class UserEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "firstName", nullable = false)
    private String firstName;

    @Column(name = "lastName", nullable = false)
    private String lastName;

    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    private boolean init = false;

    // Empty constructor for JPA
    protected UserEntity() {
    }
    
    public boolean init(String firstName, String lastName)
    {
        if (firstName == null || lastName == null) return false;

        if (!init) {
            init = true;
            this.firstName = firstName;
            this.lastName = lastName;
            this.balance = BigDecimal.ZERO;
            return true;
        }
        return false;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public boolean deposit(BigDecimal amount) {
        if (amount == null) return false;

        this.balance = this.balance.add(amount);
        return true;
    }

    public boolean withdraw(BigDecimal amount) {
        return deposit(amount.negate());
    }

    public boolean changeFirstName(String firstName) {
        if (firstName == null) return false;

        this.firstName = firstName;
        return true;
    }

    public boolean changeLastName(String lastName) {
        if (lastName == null) return false;

        this.lastName = lastName;
        return true;
    }

}
