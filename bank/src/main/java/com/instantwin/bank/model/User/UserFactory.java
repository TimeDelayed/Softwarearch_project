package com.instantwin.bank.model.User;

import com.instantwin.bank.contract.Model.User.IUserFactory;

public class UserFactory implements IUserFactory {

    @Override
    public UserEntity createUser(String firstName, String lastName) {
        return UserEntity.of(firstName, lastName);
    }

}
