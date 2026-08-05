package com.instantwin.bank.contract.Controller.User;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.instantwin.bank.DTO.User.UserDTO;
import com.instantwin.bank.contract.View.User.IUserDeleteView;
import com.instantwin.bank.contract.View.User.IUserView;
import com.instantwin.bank.view.User.UserDeleteView;
import com.instantwin.bank.view.User.UserExistsView;
import com.instantwin.bank.view.User.UserView;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "User Management", description = "Operations for creating, updating, retrieving and managing bank users.")
@RequestMapping("/instantwin/bank/api")
public interface IUserController {

        @Operation(summary = "Get all users", description = "Returns all registered users including their calculated account balances.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Users successfully retrieved; returns an empty list when no users exist",
                                        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                        array = @ArraySchema(schema = @Schema(implementation = UserView.class)))),
                        @ApiResponse(responseCode = "500", description = "User balances could not be retrieved from the Transaction slice",
                                        content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE,
                                                        schema = @Schema(type = "string")))
        })
        @GetMapping("/users")
        ResponseEntity<List<IUserView>> findAllUsers();

        @Operation(summary = "Get user by ID", description = "Returns a single user together with the current calculated account balance.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "User found",
                                        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                        schema = @Schema(implementation = UserView.class))),
                        @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
                        @ApiResponse(responseCode = "500", description = "User balance could not be retrieved from the Transaction slice",
                                        content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE,
                                                        schema = @Schema(type = "string")))
        })
        @GetMapping("/user/{id}")
        ResponseEntity<IUserView> findUserById(
                        @Parameter(description = "ID of the user", example = "1") @PathVariable long id);

        @Operation(summary = "Check user existence", description = "Lightweight endpoint used by the Transaction slice to verify whether a user exists without triggering balance calculations.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "User exists",
                                        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                        schema = @Schema(implementation = UserExistsView.class))),
                        @ApiResponse(responseCode = "404", description = "User does not exist", content = @Content)
        })
        @GetMapping("/user/{id}/exists")
        ResponseEntity<UserExistsView> checkIfUserExists(
                        @Parameter(description = "ID of the user whose existence is checked", example = "1") @PathVariable long id);

        @Operation(summary = "Create user", description = "Creates a new user account.")
        @ApiResponses({
                        @ApiResponse(responseCode = "201", description = "User successfully created",
                                        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                        schema = @Schema(implementation = UserView.class))),
                        @ApiResponse(responseCode = "400", description = "First name or last name is null, blank or otherwise invalid",
                                        content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE,
                                                        schema = @Schema(type = "string")))
        })
        @PostMapping("/user")
        ResponseEntity<IUserView> createUser(
                        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "First and last name of the user to create", required = true)
                        @RequestBody @Valid UserDTO userDTO);

        @Operation(summary = "Update user", description = "Updates the first and last name of an existing user.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "User updated",
                                        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                        schema = @Schema(implementation = UserView.class))),
                        @ApiResponse(responseCode = "400", description = "First name or last name is null, blank or otherwise invalid",
                                        content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE,
                                                        schema = @Schema(type = "string"))),
                        @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
                        @ApiResponse(responseCode = "500", description = "Updated user balance could not be retrieved from the Transaction slice",
                                        content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE,
                                                        schema = @Schema(type = "string")))
        })
        @PutMapping("/user/{id}")
        ResponseEntity<IUserView> updateUserName(
                        @Parameter(description = "ID of the user to update", example = "1") @PathVariable long id,
                        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "New first and last name of the user", required = true)
                        @RequestBody @Valid UserDTO userDTO);

        @Operation(summary = "Delete user", description = "Deletes a user and returns information about the removed account.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "User deleted",
                                        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                        schema = @Schema(implementation = UserDeleteView.class))),
                        @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
                        @ApiResponse(responseCode = "500", description = "User balance could not be retrieved from the Transaction slice before deletion",
                                        content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE,
                                                        schema = @Schema(type = "string")))
        })
        @DeleteMapping("/user/{id}")
        ResponseEntity<IUserDeleteView> deleteUser(
                        @Parameter(description = "ID of the user to delete", example = "1") @PathVariable long id);

        @Operation(summary = "Deposit funds", description = "Creates a deposit transaction for the specified user.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Deposit successful",
                                        content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE,
                                                        schema = @Schema(type = "string"))),
                        @ApiResponse(responseCode = "400", description = "Amount is negative or decimals are outside the range from 0 to 99",
                                        content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE,
                                                        schema = @Schema(type = "string"))),
                        @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
                        @ApiResponse(responseCode = "500", description = "Transaction could not be created",
                                        content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE,
                                                        schema = @Schema(type = "string")))
        })
        @PostMapping(value = "/user/{id}/deposit/{amount}/{decimals}", produces = MediaType.TEXT_PLAIN_VALUE)
        ResponseEntity<String> depositToUser(
                        @Parameter(description = "ID of the user receiving the deposit", example = "1") @PathVariable long id,
                        @Parameter(description = "Non-negative whole or decimal amount", example = "50") @PathVariable BigDecimal amount,
                        @Parameter(description = "Additional hundredths added to the amount, from 0 to 99", example = "75") @PathVariable int decimals);

        @Operation(summary = "Withdraw funds", description = "Creates a withdrawal transaction for the specified user.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Withdrawal successful",
                                        content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE,
                                                        schema = @Schema(type = "string"))),
                        @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
                        @ApiResponse(responseCode = "400", description = "Amount is negative or decimals are outside the range from 0 to 99",
                                        content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE,
                                                        schema = @Schema(type = "string"))),
                        @ApiResponse(responseCode = "500", description = "Transaction could not be created",
                                        content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE,
                                                        schema = @Schema(type = "string")))
        })
        @PostMapping(value = "/user/{id}/withdraw/{amount}/{decimals}", produces = MediaType.TEXT_PLAIN_VALUE)
        ResponseEntity<String> withdrawFromUser(
                        @Parameter(description = "ID of the user making the withdrawal", example = "1") @PathVariable long id,
                        @Parameter(description = "Non-negative whole or decimal amount", example = "30") @PathVariable BigDecimal amount,
                        @Parameter(description = "Additional hundredths added to the amount, from 0 to 99", example = "25") @PathVariable int decimals);

}
