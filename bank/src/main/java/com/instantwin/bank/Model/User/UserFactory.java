package com.instantwin.bank.Model.User;

import com.instantwin.bank.Utilities.ModelValidityBreachException;
import com.instantwin.bank.contract.Model.User.IUserFactory;

public class UserFactory implements IUserFactory {

    @Override
    public UserEntity createUser(String firstName, String lastName) throws ModelValidityBreachException {
        return UserEntity.of(firstName, lastName);
    }
    
}
