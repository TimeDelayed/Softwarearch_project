package com.instantwin.bank.contract.Service.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;

import com.instantwin.bank.contract.DTO.User.IUserDTO;
import com.instantwin.bank.contract.View.User.IUserDeleteView;
import com.instantwin.bank.contract.View.User.IUserView;

public interface IUserService {

    List<IUserView> findAllUsers();

    Optional<IUserView> findUserById(long id);

    IUserView createUser(IUserDTO userDTO);

    Optional<IUserView> updateUserName(long id, IUserDTO userDTO);

    Optional<IUserDeleteView> deleteUser(long id);

    ResponseEntity<String> depositToUser(long id, BigDecimal amount);

    ResponseEntity<String> withdrawFromUser(long id, BigDecimal amount);
}
