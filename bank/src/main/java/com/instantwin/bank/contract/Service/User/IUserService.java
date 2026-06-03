package com.instantwin.bank.contract.Service.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.instantwin.bank.Utilities.InsufficientBalanceException;
import com.instantwin.bank.contract.DTO.IUserDTO;
import com.instantwin.bank.contract.View.User.IUserDeleteView;
import com.instantwin.bank.contract.View.User.IUserView;

public interface IUserService {

    List<IUserView> findAllUsers();

    Optional<IUserView> findUserById(long id);

    IUserView createUser(IUserDTO userDTO);

    Optional<IUserView> updateUserName(long id, IUserDTO userDTO);

    Optional<IUserDeleteView> deleteUser(long id);

    Optional<IUserView> depositToUser(long id, BigDecimal amount);

    Optional<IUserView> withdrawFromUser(long id, BigDecimal amount) throws InsufficientBalanceException;
}
