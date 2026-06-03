package com.instantwin.bank.contract.Controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.instantwin.bank.DTO.User.UserDTO;
import com.instantwin.bank.Utilities.InsufficientBalanceException;
import com.instantwin.bank.contract.DTO.IUserDTO;
import com.instantwin.bank.contract.View.User.IUserDeleteView;
import com.instantwin.bank.contract.View.User.IUserView;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@RequestMapping("/instantwin/bank/api")
public interface IUserController {

    @GetMapping("/users")
    ResponseEntity<List<IUserView>> findAllUsers();

    @GetMapping("/user/{id}")
    ResponseEntity<IUserView> findUserById(@PathVariable long id);

    @PostMapping("/user")
    ResponseEntity<IUserView> createUser(@RequestBody @Valid UserDTO userDTO);

    @PutMapping("/user/{id}")
    ResponseEntity<IUserView> updateUserName(@PathVariable long id, @RequestBody @Valid IUserDTO userDTO);

    @DeleteMapping("/user/{id}")
    ResponseEntity<IUserDeleteView> deleteUser(@PathVariable long id);

    @PutMapping("/user/{id}/deposit/{amount}/{decimals}")
    ResponseEntity<IUserView> depositToUser(@PathVariable long id, @PathVariable BigDecimal amount,
            @PathVariable int decimals);

    @PutMapping("/user/{id}/withdraw/{amount}/{decimals}")
    ResponseEntity<IUserView> withdrawFromUser(@PathVariable long id, @PathVariable BigDecimal amount,
            @PathVariable int decimals) throws InsufficientBalanceException;

}
