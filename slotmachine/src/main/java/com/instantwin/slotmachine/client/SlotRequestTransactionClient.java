package com.instantwin.slotmachine.client;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import com.instantwin.slotmachine.contract.client.ISlotRequestTransactionClient;
import com.instantwin.slotmachine.dto.SlotRequestTransactionDTO;

@Component
public class SlotRequestTransactionClient implements ISlotRequestTransactionClient {

    private final String invoicingParty = "SLOTS";

    private final RestClient restClient;

    public SlotRequestTransactionClient(
            RestClient.Builder builder,
            @Value("${BANK_SERVICE_URL:http://localhost:8081}") String bankServiceUrl) {

        this.restClient = builder
                .baseUrl(bankServiceUrl + "/instantwin/bank/api")
                .build();
    }

    @Override
    public ResponseEntity<String> requestTransaction(long userId, BigDecimal amount) {
        try {
            return restClient.post()
                    .uri("/transaction/user/{userId}", userId)
                    .body(new SlotRequestTransactionDTO(invoicingParty, amount))
                    .retrieve()
                    .toEntity(String.class);

        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.notFound().build();
        }
    }
}
