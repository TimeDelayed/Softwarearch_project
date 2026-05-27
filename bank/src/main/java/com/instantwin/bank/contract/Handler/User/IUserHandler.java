package com.instantwin.bank.contract.Handler.User;

import java.util.List;
import java.util.Optional;

import com.instantwin.bank.Utilities.ModelValidityBreachException;
import com.instantwin.bank.contract.DTO.IUserDTO;
import com.instantwin.bank.contract.View.User.IUserDeleteView;
import com.instantwin.bank.contract.View.User.IUserView;

public interface IUserHandler {

    List<IUserView> findAllUsers();

    Optional<IUserView> findUserById(long id);

    IUserView createUser(IUserDTO userDTO);

    Optional<IUserView> updateUserName(long id, IUserDTO userDTO);

    Optional<IUserDeleteView> deleteUser(long id);
}
