package com.instantwin.bank.Client;

import java.util.List;
import java.util.Optional;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import com.instantwin.bank.DTO.User.UserRequestTransaction;

@Component
public class UserTransactionClient {

    private static final String BASE_URL = "http://localhost:8081/instantwin/bank/api";

    private final RestClient restClient;

    public UserTransactionClient(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl(BASE_URL)
                .build();
    }

    public Optional<List<UserRequestTransaction>> getAllTransactionsForUser(long userId) {
        try {
            List<UserRequestTransaction> result = restClient.get()
                    .uri("transactions/user/{id}", userId)
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<UserRequestTransaction>>() {
                    });

            return Optional.of(result);
        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        }
    }

}
