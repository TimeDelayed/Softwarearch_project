package com.instantwin.bank.contract.Client;

import java.util.List;
import java.util.Optional;

import com.instantwin.bank.DTO.User.UserRequestTransaction;


public interface IUserTransactionClient {
    Optional<List<UserRequestTransaction>> getAllTransactionsForUser(long userId);
}
