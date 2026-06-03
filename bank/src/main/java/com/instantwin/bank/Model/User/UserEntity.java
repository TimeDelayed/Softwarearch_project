package com.instantwin.bank.Model.User;

import java.math.BigDecimal;

import com.instantwin.bank.Utilities.ErrorMessages;
import com.instantwin.bank.Utilities.ModelValidityBreachException;
import com.instantwin.bank.Utilities.InsufficientBalanceException;
import com.instantwin.bank.Utilities.TransactionNumberInvalidException;
import com.instantwin.bank.contract.Model.User.IUserEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "users")
@Getter
public class UserEntity implements IUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "firstName", nullable = false)
    private String firstName;

    @Column(name = "lastName", nullable = false)
    private String lastName;

    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    // Empty constructor for JPA
    protected UserEntity() {
    }

    private UserEntity(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.balance = BigDecimal.ZERO;
    }

    private static void validateUserName(String name) {
        if (name == null || name.isBlank())
            throw new ModelValidityBreachException(ErrorMessages.USER_NAME_IS_INVALID);
    }

    public static UserEntity of(String firstName, String lastName) {
        validateUserName(firstName);
        validateUserName(lastName);

        return new UserEntity(firstName, lastName);
    }

    private void validateAmount(BigDecimal amount) throws TransactionNumberInvalidException {
        if (amount == null)
            throw new TransactionNumberInvalidException(ErrorMessages.CURRENCY_INPUT_INVALID);

        boolean isNegative = amount.signum() == -1;

        if (isNegative)
            throw new TransactionNumberInvalidException(ErrorMessages.CURRENCY_INPUT_INVALID);
    }

    private void validateNotNegativeBalance(BigDecimal amount) throws InsufficientBalanceException {
        boolean wouldBeNegative = this.balance.subtract(amount).signum() == -1;
        if (wouldBeNegative)
            throw new InsufficientBalanceException(ErrorMessages.NEGATIVE_BALANCE_ERROR);
    }

    public void deposit(BigDecimal amount) throws TransactionNumberInvalidException {
        validateAmount(amount);

        this.balance = this.balance.add(amount);
    }

    public void withdraw(BigDecimal amount) throws TransactionNumberInvalidException, InsufficientBalanceException {
        validateAmount(amount);
        validateNotNegativeBalance(amount);

        this.balance = this.balance.subtract(amount);
    }

    public void changeFirstName(String firstName) {
        validateUserName(firstName);

        this.firstName = firstName;
    }

    public void changeLastName(String lastName) {
        validateUserName(lastName);

        this.lastName = lastName;
    }

}
