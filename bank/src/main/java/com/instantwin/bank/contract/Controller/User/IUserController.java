package com.instantwin.bank.contract.Controller.User;

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
import com.instantwin.bank.View.User.UserExistsView;
import com.instantwin.bank.contract.View.User.IUserDeleteView;
import com.instantwin.bank.contract.View.User.IUserView;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "User Management", description = "Operations for creating, updating, retrieving and managing bank users.")
@RequestMapping("/instantwin/bank/api")
public interface IUserController {

        @Operation(summary = "Get all users", description = "Returns all registered users including their calculated account balances.")
        @ApiResponse(responseCode = "200", description = "Users successfully retrieved")
        @GetMapping("/users")
        ResponseEntity<List<IUserView>> findAllUsers();

        @Operation(summary = "Get user by ID", description = "Returns a single user together with the current calculated account balance.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "User found"),
                        @ApiResponse(responseCode = "404", description = "User not found")
        })
        @GetMapping("/user/{id}")
        ResponseEntity<IUserView> findUserById(@PathVariable long id);

        @Operation(summary = "Check user existence", description = "Lightweight endpoint used by the Transaction slice to verify whether a user exists without triggering balance calculations.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "User exists"),
                        @ApiResponse(responseCode = "404", description = "User does not exist")
        })
        @GetMapping("/user/{id}/exists")
        ResponseEntity<UserExistsView> checkIfUserExists(@PathVariable long id);

        @Operation(summary = "Create user", description = "Creates a new user account.")
        @ApiResponse(responseCode = "201", description = "User successfully created")
        @PostMapping("/user")
        ResponseEntity<IUserView> createUser(@RequestBody @Valid UserDTO userDTO);

        @Operation(summary = "Update user", description = "Updates the first and last name of an existing user.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "User updated"),
                        @ApiResponse(responseCode = "404", description = "User not found")
        })
        @PutMapping("/user/{id}")
        ResponseEntity<IUserView> updateUserName(@PathVariable long id, @RequestBody @Valid UserDTO userDTO);

        @Operation(summary = "Delete user", description = "Deletes a user and returns information about the removed account.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "User deleted"),
                        @ApiResponse(responseCode = "404", description = "User not found")
        })
        @DeleteMapping("/user/{id}")
        ResponseEntity<IUserDeleteView> deleteUser(@PathVariable long id);

        @Operation(summary = "Deposit funds", description = "Creates a deposit transaction for the specified user.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Deposit successful"),
                        @ApiResponse(responseCode = "404", description = "User not found")
        })
        @PostMapping("/user/{id}/deposit/{amount}/{decimals}")
        ResponseEntity<String> depositToUser(
                        @PathVariable long id,
                        @PathVariable BigDecimal amount,
                        @PathVariable int decimals);

        @Operation(summary = "Withdraw funds", description = "Creates a withdrawal transaction for the specified user.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Withdrawal successful"),
                        @ApiResponse(responseCode = "404", description = "User not found"),
                        @ApiResponse(responseCode = "400", description = "Insufficient balance")
        })
        @PostMapping("/user/{id}/withdraw/{amount}/{decimals}")
        ResponseEntity<String> withdrawFromUser(
                        @PathVariable long id,
                        @PathVariable BigDecimal amount,
                        @PathVariable int decimals);

}