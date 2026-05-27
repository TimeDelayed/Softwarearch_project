package com.instantwin.bank.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.instantwin.bank.DTO.UserDTO;
import com.instantwin.bank.contract.Controller.IUserController;
import com.instantwin.bank.contract.DTO.IUserDTO;
import com.instantwin.bank.contract.Handler.User.IUserHandler;
import com.instantwin.bank.contract.View.User.IUserDeleteView;
import com.instantwin.bank.contract.View.User.IUserView;

@RestController
public class UserController implements IUserController {

    private final IUserHandler userHandler;

    public UserController(IUserHandler userHandler) {
        this.userHandler = userHandler;
    }

    @Override
    public ResponseEntity<List<IUserView>> findAllUsers() {
        // Fix if empty list is returned, should not be an error
        return ResponseEntity.ok(userHandler.findAllUsers());
    }

    @Override
    public ResponseEntity<IUserView> findUserById(long id) {
        var result = userHandler.findUserById(id);
        if (result.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result.get());
    }

    @Override
    public ResponseEntity<IUserView> createUser(UserDTO userDTO) {
        var result = userHandler.createUser(userDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @Override
    public ResponseEntity<IUserView> updateUserName(long id, IUserDTO userDTO) {
        var result = userHandler.updateUserName(id, userDTO);
        if (result.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result.get());
    }

    @Override
    public ResponseEntity<IUserDeleteView> deleteUser(long id) {
        var result = userHandler.deleteUser(id);
        if (result.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result.get());
    }

}
