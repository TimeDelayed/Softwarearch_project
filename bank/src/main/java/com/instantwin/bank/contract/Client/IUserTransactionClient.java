package com.instantwin.bank.contract.Client;

import java.util.List;
import java.util.Optional;

import com.instantwin.bank.contract.DTO.IUserRequestTransaction;

public interface IUserTransactionClient {
    Optional<List<IUserRequestTransaction>> getAllTransactionsForUser(long userId);
}
