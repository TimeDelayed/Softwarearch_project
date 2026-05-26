package com.instantwin.bank.contract.Handler.User;

import java.util.List;
import java.util.Optional;

import com.instantwin.bank.contract.DTO.IUserDTO;
import com.instantwin.bank.contract.View.User.IUserView;


public interface IUserHandler {

    List<IUserView> findAll();
    
    Optional<IUserView> findById(long id);

    Optional<IUserView> createUser(IUserDTO userDTO);
}
