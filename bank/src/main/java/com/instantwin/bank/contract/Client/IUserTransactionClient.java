package com.instantwin.bank.contract.Client;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;

import com.instantwin.bank.DTO.User.UserRequestTransaction;


public interface IUserTransactionClient {

    Optional<List<UserRequestTransaction>> getAllTransactionsForUser(long userId);

    ResponseEntity<String> depositTransaction(long userId, BigDecimal amount);
    ResponseEntity<String> withdrawTransaction(long userId, BigDecimal amount);
}
