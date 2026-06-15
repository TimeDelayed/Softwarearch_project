package com.instantwin.bank.contract.Controller.Transaction;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.instantwin.bank.DTO.Transaction.TransactionDTO;
import com.instantwin.bank.DTO.Transaction.TransactionUpdateDTO;
import com.instantwin.bank.View.Transaction.TransactionDeleteView;
import com.instantwin.bank.View.Transaction.TransactionSpecificUserView;
import com.instantwin.bank.View.Transaction.TransactionView;

import jakarta.validation.Valid;

@RequestMapping("/instantwin/bank/api")
public interface ITransactionController {

    @GetMapping("/transactions")
    ResponseEntity<List<TransactionView>> findAllTransactions();

    @GetMapping("/transactions/user/{userId}")
    ResponseEntity<List<TransactionSpecificUserView>> findTransactionsByUserId(long userId);

    @PostMapping("/transaction/user/{userId}")
    ResponseEntity<TransactionView> createTransaction(long userId,
            @Valid @RequestBody TransactionDTO transactionRequest);

    @PutMapping("/transaction/{transactionId}")
    ResponseEntity<TransactionView> updateTransaction(long transactionId,
            @Valid @RequestBody TransactionUpdateDTO transactionRequest);
    
    @DeleteMapping("/transaction/{transactionId}")
    ResponseEntity<TransactionDeleteView> deleteTransaction(long transactionId);


}
