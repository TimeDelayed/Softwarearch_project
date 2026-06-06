package com.instantwin.bank.contract.Model.User;

public interface IUserEntity {

    Long getId();

    String getFirstName();

    String getLastName();

    void changeFirstName(String firstName);

    void changeLastName(String lastName);
}
