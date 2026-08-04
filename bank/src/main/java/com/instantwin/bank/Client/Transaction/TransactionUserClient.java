package com.instantwin.bank.client.Transaction;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import com.instantwin.bank.DTO.Transaction.TransactionRequestUser;
import com.instantwin.bank.contract.Client.Transaction.ITransactionUserClient;

@Component
public class TransactionUserClient implements ITransactionUserClient {
    private final RestClient restClient;

    public TransactionUserClient(
            RestClient.Builder builder,
            @Value("${BANK_SERVICE_URL:http://localhost:8081}") String bankServiceUrl) {

        this.restClient = builder
                .baseUrl(bankServiceUrl + "/instantwin/bank/api")
                .build();
    }

    @Override
    public Optional<TransactionRequestUser> checkIfUserExists(long userId) {
        try {
            var user = restClient.get()
                    .uri("/user/{id}/exists", userId)
                    .retrieve()
                    .toEntity(TransactionRequestUser.class);

            return Optional.ofNullable(user.getBody());
        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        }
    }

}
