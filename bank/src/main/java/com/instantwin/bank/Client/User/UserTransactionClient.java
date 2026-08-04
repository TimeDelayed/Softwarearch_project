package com.instantwin.bank.client.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import com.instantwin.bank.DTO.User.UserRequestTransaction;
import com.instantwin.bank.DTO.User.UserTransactionDTO;
import com.instantwin.bank.contract.Client.User.IUserTransactionClient;
import com.instantwin.bank.utilities.User.UserInvoicingParty;

@Component
public class UserTransactionClient implements IUserTransactionClient {

    private final UserInvoicingParty invoicingParty = UserInvoicingParty.USER_SLICE;

    private final RestClient restClient;

    public UserTransactionClient(
            RestClient.Builder builder,
            @Value("${BANK_SERVICE_URL:http://localhost:8081}") String bankServiceUrl) {

        this.restClient = builder
                .baseUrl(bankServiceUrl + "/instantwin/bank/api")
                .build();
    }

    @Override
    public Optional<List<UserTransactionDTO>> getAllTransactionsForUser(long userId) {
        try {
            List<UserTransactionDTO> result = restClient.get()
                    .uri("/transactions/user/{id}", userId)
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<UserTransactionDTO>>() {
                    });

            return Optional.of(result);
        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        }
    }

    @Override
    public ResponseEntity<String> depositTransaction(long userId, BigDecimal amount) {
        try {
            var responseAsString = restClient.post()
                    .uri("/transaction/user/{userId}", userId)
                    .body(new UserRequestTransaction(
                            invoicingParty, amount))
                    .retrieve()
                    .toEntity(String.class);
            return responseAsString;
        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.notFound().build();
        }

    }

    @Override
    public ResponseEntity<String> withdrawTransaction(long userId, BigDecimal amount) {
        try {
            BigDecimal negativeAmount = amount.negate();
            var responseAsString = restClient.post()
                    .uri("/transaction/user/{id}", userId)
                    .body(new UserRequestTransaction(
                            invoicingParty,
                            negativeAmount))
                    .retrieve()
                    .toEntity(String.class);

            return responseAsString;
        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.notFound().build();
        }
    }

}
