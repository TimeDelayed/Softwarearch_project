package com.instantwin.bank.service.Transaction;

import java.util.List;
import java.util.Optional;

import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;

import com.instantwin.bank.DTO.Transaction.TransactionDTO;
import com.instantwin.bank.DTO.Transaction.TransactionRequestUser;
import com.instantwin.bank.DTO.Transaction.TransactionUpdateDTO;
import com.instantwin.bank.contract.Client.Transaction.ITransactionUserClient;
import com.instantwin.bank.contract.Model.Transaction.ITransactionFactory;
import com.instantwin.bank.contract.Service.Transaction.ITransactionService;
import com.instantwin.bank.repository.Transaction.ITransactionRepository;
import com.instantwin.bank.view.Transaction.TransactionDeleteView;
import com.instantwin.bank.view.Transaction.TransactionSpecificUserView;
import com.instantwin.bank.view.Transaction.TransactionView;

@Service
public class TransactionService implements ITransactionService {

    private final ITransactionRepository transactionRepository;
    private final ITransactionUserClient userTransactionClient;
    private final ITransactionFactory transactionFactory;

    public TransactionService(
            ITransactionRepository transactionRepository,
            ITransactionUserClient userTransactionClient,
            ITransactionFactory transactionFactory) {
        this.transactionRepository = transactionRepository;
        this.userTransactionClient = userTransactionClient;
        this.transactionFactory = transactionFactory;
    }

    @Override
    public List<TransactionView> findAllTransactions() {
        var transactions = transactionRepository.findAll();
        return transactions.stream().map(TransactionView::of).toList();
    }

    @Override
    public Optional<List<TransactionSpecificUserView>> findTransactionsByUserId(long userId) {
        var userExists = userTransactionClient.checkIfUserExists(userId);
        if (userExists.isEmpty()) {
            return Optional.empty();
        }
        var transactions = transactionRepository.findAllByUserId(userId);
        return Optional.of(transactions.stream().map(TransactionSpecificUserView::of).toList());
    }

    @Override
    public Optional<TransactionView> createTransaction(long userId, TransactionDTO transactionRequest) {
        var userExists = userTransactionClient.checkIfUserExists(userId);
        if (userExists.isEmpty()) {
            return Optional.empty();
        }

        var transactionEntity = transactionFactory.createTransaction(
                userId,
                transactionRequest.amount(), transactionRequest.invoicingParty());
        var savedTransaction = transactionRepository.save(transactionEntity);
        return Optional.of(TransactionView.of(savedTransaction));
    }

    @Override
    public Optional<TransactionView> updateTransaction(long transactionId, TransactionUpdateDTO transactionRequest) {
        var transaction = transactionRepository.findById(transactionId);
        if (transaction.isEmpty()) {
            return Optional.empty();
        }
        var validUser = userTransactionClient.checkIfUserExists(transactionRequest.userId());
        if (validUser.isEmpty()) {
            return Optional.empty();
        }
        var transactionEntity = transaction.get();
        transactionEntity.updateAmount(transactionRequest.amount());
        transactionEntity.updateInvoicingParty(transactionRequest.invoicingParty());
        transactionEntity.updateUserId(transactionRequest.userId());
        var updatedTransaction = transactionRepository.save(transactionEntity);
        return Optional.of(TransactionView.of(updatedTransaction));
    }

    @Override
    public Optional<TransactionView> getTransactionById(long transactionId) {
        var transaction = transactionRepository.findById(transactionId);
        if (transaction.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(TransactionView.of(transaction.get()));
    }

    @Override
    public Optional<TransactionRequestUser> checkIfUserExists(long userId) {
        return userTransactionClient.checkIfUserExists(userId);
    }

    @Override
    public Optional<TransactionDeleteView> deleteTransaction(long transactionId) {
        var transaction = transactionRepository.findById(transactionId);
        if (transaction.isEmpty()) {
            return Optional.empty();
        }

        transactionRepository.delete(transaction.get());
        return Optional.of(TransactionDeleteView.of(transaction.get()));
    }

}
