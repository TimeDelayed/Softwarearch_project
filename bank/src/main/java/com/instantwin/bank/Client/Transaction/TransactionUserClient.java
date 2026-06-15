package com.instantwin.bank.Client.Transaction;

import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.instantwin.bank.DTO.Transaction.TransactionRequestUser;
import com.instantwin.bank.contract.Client.Transaction.ITransactionUserClient;

@Component
public class TransactionUserClient implements ITransactionUserClient {

    private static final String BASE_URL = "http://bank:8080/instantwin/bank/api";
    private final RestClient restClient;

    public TransactionUserClient(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl(BASE_URL)
                .build();
    }

    @Override
    public Optional<TransactionRequestUser> checkIfUserExists(long userId) {
        try {
            var user = restClient.get()
                    .uri("/users/{id}", userId)
                    .retrieve()
                    .toEntity(TransactionRequestUser.class);

            return Optional.of(user.getBody());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

}
