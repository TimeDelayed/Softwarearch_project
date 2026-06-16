package com.instantwin.bank.contract.Client.Transaction;

import java.util.Optional;

import com.instantwin.bank.DTO.Transaction.TransactionRequestUser;

public interface ITransactionUserClient {
    Optional<TransactionRequestUser> checkIfUserExists(long userId);
}
