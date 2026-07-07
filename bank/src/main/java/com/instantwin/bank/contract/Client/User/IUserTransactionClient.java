package com.instantwin.bank.contract.Client.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import com.instantwin.bank.DTO.User.UserTransactionDTO;

public interface IUserTransactionClient {

    Optional<List<UserTransactionDTO>> getAllTransactionsForUser(long userId);

    ResponseEntity<String> depositTransaction(long userId, BigDecimal amount);

    ResponseEntity<String> withdrawTransaction(long userId, BigDecimal amount);
}
