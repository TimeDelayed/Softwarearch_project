package com.instantwin.bank.Model.User;

import com.instantwin.bank.Utilities.User.ModelValidityBreachException;
import com.instantwin.bank.Utilities.User.UserErrorMessages;
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

    // Empty constructor for JPA
    protected UserEntity() {
    }

    private UserEntity(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    private static void validateUserName(String name) {
        if (name == null || name.isBlank())
            throw new ModelValidityBreachException(UserErrorMessages.USER_NAME_IS_INVALID);
    }

    public static UserEntity of(String firstName, String lastName) {
        validateUserName(firstName);
        validateUserName(lastName);

        return new UserEntity(firstName, lastName);
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
