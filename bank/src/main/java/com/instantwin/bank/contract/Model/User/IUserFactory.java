package com.instantwin.bank.contract.Model.User;

import com.instantwin.bank.Model.User.UserEntity;
import com.instantwin.bank.Utilities.ModelValidityBreachException;

public interface IUserFactory {
    UserEntity createUser(String firstName, String lastName) throws ModelValidityBreachException;
}
