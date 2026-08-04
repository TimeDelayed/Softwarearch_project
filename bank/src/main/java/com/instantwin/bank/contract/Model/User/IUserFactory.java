package com.instantwin.bank.contract.Model.User;

import com.instantwin.bank.model.User.UserEntity;

public interface IUserFactory {
    UserEntity createUser(String firstName, String lastName);
}
