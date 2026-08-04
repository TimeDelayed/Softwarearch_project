package com.instantwin.bank.contract.Controller.Transaction;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.instantwin.bank.DTO.Transaction.TransactionDTO;
import com.instantwin.bank.DTO.Transaction.TransactionUpdateDTO;
import com.instantwin.bank.view.Transaction.TransactionDeleteView;
import com.instantwin.bank.view.Transaction.TransactionSpecificUserView;
import com.instantwin.bank.view.Transaction.TransactionView;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Transaction Management", description = "Operations for creating, updating and retrieving financial transactions.")
@RequestMapping("/instantwin/bank/api")
public interface ITransactionController {

        @Operation(summary = "Get all transactions", description = "Returns all transactions stored in the system.")
        @ApiResponse(responseCode = "200", description = "Transactions successfully retrieved")
        @GetMapping("/transactions")
        ResponseEntity<List<TransactionView>> findAllTransactions();

        @Operation(summary = "Get transactions for user", description = "Returns all transactions belonging to a specific user.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Transactions found"),
                        @ApiResponse(responseCode = "404", description = "User not found"),
                        @ApiResponse(responseCode = "500", description = "User existence could not be verified")
        })
        @GetMapping("/transactions/user/{userId}")
        ResponseEntity<List<TransactionSpecificUserView>> findTransactionsByUserId(
                        @Parameter(description = "ID of the user whose transactions are requested", example = "1") @PathVariable long userId);

        @Operation(summary = "Create transaction", description = "Creates a new transaction for a specific user.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Transaction created"),
                        @ApiResponse(responseCode = "400", description = "Amount or invoicing party is invalid"),
                        @ApiResponse(responseCode = "404", description = "User not found"),
                        @ApiResponse(responseCode = "500", description = "User existence could not be verified")
        })
        @PostMapping("/transaction/user/{userId}")
        ResponseEntity<TransactionView> createTransaction(
                        @Parameter(description = "ID of the user receiving the transaction", example = "1") @PathVariable long userId,
                        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Transaction amount and invoicing party", required = true)
                        @Valid @RequestBody TransactionDTO transactionRequest);

        @Operation(summary = "Update transaction", description = "Updates an existing transaction.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Transaction updated"),
                        @ApiResponse(responseCode = "400", description = "User ID, amount or invoicing party is invalid"),
                        @ApiResponse(responseCode = "404", description = "Transaction or target user not found"),
                        @ApiResponse(responseCode = "500", description = "Target user existence could not be verified")
        })
        @PutMapping("/transaction/{transactionId}")
        ResponseEntity<TransactionView> updateTransaction(
                        @Parameter(description = "ID of the transaction to update", example = "1") @PathVariable long transactionId,
                        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Replacement user ID, amount and invoicing party", required = true)
                        @Valid @RequestBody TransactionUpdateDTO transactionRequest);

        @Operation(summary = "Delete transaction", description = "Deletes an existing transaction.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Transaction deleted"),
                        @ApiResponse(responseCode = "404", description = "Transaction not found")
        })
        @DeleteMapping("/transaction/{transactionId}")
        ResponseEntity<TransactionDeleteView> deleteTransaction(
                        @Parameter(description = "ID of the transaction to delete", example = "1") @PathVariable long transactionId);

}
