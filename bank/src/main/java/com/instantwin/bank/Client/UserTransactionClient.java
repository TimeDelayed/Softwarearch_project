package com.instantwin.bank.Client;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import com.instantwin.bank.DTO.User.UserRequestTransaction;
import com.instantwin.bank.Utilities.InvoicingParty;
import com.instantwin.bank.contract.Client.IUserTransactionClient;

@Component
public class UserTransactionClient implements IUserTransactionClient {

    private final InvoicingParty invoicingParty = InvoicingParty.USER_SLICE;

    private static final String BASE_URL = "http://localhost:8081/instantwin/bank/api";

    private final RestClient restClient;

    public UserTransactionClient(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl(BASE_URL)
                .build();
    }
    // TODO: FRAGEN WIE SOWAS TESTEN
    @Override
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

    @Override
    public ResponseEntity<String> depositTransaction(long userId, BigDecimal amount) {
        var responseAsString = restClient.post()
                .uri("/transaction/user/{id}", userId)
                .body(new UserRequestTransaction(
                        invoicingParty, amount))
                .retrieve()
                .toEntity(String.class);

        return responseAsString;
    }

    @Override
    public ResponseEntity<String> withdrawTransaction(long userId, BigDecimal amount) {
        BigDecimal negativeAmount = amount.negate();
        var responseAsString = restClient.post()
                .uri("/transaction/user/{id}", userId)
                .body(new UserRequestTransaction(
                        invoicingParty,
                        negativeAmount))
                .retrieve()
                .toEntity(String.class);

        return responseAsString;
    }

}
