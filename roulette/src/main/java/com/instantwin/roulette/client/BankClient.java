package com.instantwin.roulette.client;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import com.instantwin.roulette.client.dto.BankTransactionRequest;
import com.instantwin.roulette.contract.client.IBankClient;

@Component
public class BankClient implements IBankClient {

    private static final String API_PATH = "/instantwin/bank/api";
    private final RestClient restClient;

    public BankClient(RestClient.Builder builder,
                      @Value("${BANK_SERVICE_URL:http://localhost:8081}") String bankServiceUrl) {
        this.restClient = builder
                .baseUrl(bankServiceUrl + API_PATH)
                .build();
    }

    @Override
    public ResponseEntity<String> requestTransaction(long userId, BigDecimal netAmount) {
        try {
            return restClient.post()
                    .uri("/transaction/user/{userId}", userId)
                    .body(new BankTransactionRequest(netAmount, "ROULETTE"))
                    .retrieve()
                    .toEntity(String.class);
        }
        catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.notFound().build();
        }
    }
}
