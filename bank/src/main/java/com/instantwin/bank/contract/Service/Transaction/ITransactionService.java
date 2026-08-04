package com.instantwin.bank.contract.Service.Transaction;

import java.util.List;
import java.util.Optional;

import com.instantwin.bank.DTO.Transaction.TransactionDTO;
import com.instantwin.bank.DTO.Transaction.TransactionRequestUser;
import com.instantwin.bank.DTO.Transaction.TransactionUpdateDTO;
import com.instantwin.bank.view.Transaction.TransactionDeleteView;
import com.instantwin.bank.view.Transaction.TransactionSpecificUserView;
import com.instantwin.bank.view.Transaction.TransactionView;

public interface ITransactionService {
    List<TransactionView> findAllTransactions();

    Optional<List<TransactionSpecificUserView>> findTransactionsByUserId(long userId);

    Optional<TransactionView> createTransaction(long userId, TransactionDTO transactionRequest);

    Optional<TransactionView> updateTransaction(long transactionId, TransactionUpdateDTO transactionRequest);

    Optional<TransactionView> getTransactionById(long transactionId);

    Optional<TransactionRequestUser> checkIfUserExists(long userId);

    Optional<TransactionDeleteView> deleteTransaction(long transactionId);
}
