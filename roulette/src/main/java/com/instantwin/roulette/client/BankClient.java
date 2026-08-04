package com.instantwin.roulette.client;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.instantwin.roulette.client.dto.BankTransactionRequest;
import com.instantwin.roulette.client.dto.BankTransactionResponse;
import com.instantwin.roulette.contract.client.IBankClient;

@Component
public class BankClient implements IBankClient {

    private static final String API_PATH = "/instantwin/bank/api";
    private final RestClient restClient;

    public BankClient(RestClient.Builder builder,
                      @Value("${bank.service.url}") String bankServiceUrl) {
        this.restClient = builder
                .baseUrl(bankServiceUrl + API_PATH)
                .build();
    }

    @Override
    public boolean userExists(long userId) {
        try {
            restClient.get()
                    .uri("/user/{id}/exists", userId)
                    .retrieve()
                    .toBodilessEntity();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Optional<BankTransactionResponse> createTransaction(long userId, BigDecimal amount) {
        try {
            var response = restClient.post()
                    .uri("/transaction/user/{userId}", userId)
                    .body(new BankTransactionRequest(amount, "ROULETTE"))
                    .retrieve()
                    .toEntity(BankTransactionResponse.class);
            return Optional.ofNullable(response.getBody());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
