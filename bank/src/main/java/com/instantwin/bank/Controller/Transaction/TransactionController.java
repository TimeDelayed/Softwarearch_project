package com.instantwin.bank.Controller.Transaction;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import com.instantwin.bank.DTO.Transaction.TransactionDTO;
import com.instantwin.bank.DTO.Transaction.TransactionUpdateDTO;
import com.instantwin.bank.View.Transaction.TransactionDeleteView;
import com.instantwin.bank.View.Transaction.TransactionSpecificUserView;
import com.instantwin.bank.View.Transaction.TransactionView;
import com.instantwin.bank.contract.Controller.Transaction.ITransactionController;
import com.instantwin.bank.contract.Service.Transaction.ITransactionService;

import jakarta.validation.Valid;

@Validated
@RestController
public class TransactionController implements ITransactionController {

    private final ITransactionService transactionHandler;

    public TransactionController(ITransactionService transactionHandler) {
        this.transactionHandler = transactionHandler;
    }

    @Override
    public ResponseEntity<List<TransactionView>> findAllTransactions() {
        var result = transactionHandler.findAllTransactions();
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<List<TransactionSpecificUserView>> findTransactionsByUserId(long userId) {
        var result = transactionHandler.findTransactionsByUserId(userId);
        if (result.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result.get());
    }

    @Override
    public ResponseEntity<TransactionView> createTransaction(long userId, TransactionDTO transactionRequest) {
        var result = transactionHandler.createTransaction(userId, transactionRequest);
        if (result.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result.get());
    }

    @Override
    public ResponseEntity<TransactionView> updateTransaction(long transactionId,
            @Valid TransactionUpdateDTO transactionRequest) {
        var result = transactionHandler.updateTransaction(transactionId, transactionRequest);
        if (result.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result.get());
    }

    @Override
    public ResponseEntity<TransactionDeleteView> deleteTransaction(long transactionId) {
        var result = transactionHandler.deleteTransaction(transactionId);
        if (result.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result.get());
    }
    
}
