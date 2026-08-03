package com.instantwin.slotmachine.client;

import java.math.BigDecimal;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.instantwin.slotmachine.contract.client.ISlotRequestTransactionClient;
import com.instantwin.slotmachine.dto.SlotRequestTransactionDTO;

@Component
public class SlotRequestTransactionClient implements ISlotRequestTransactionClient {

    private final String invoicingParty = "SLOTS";

    private static final String BASE_URL = "http://bank:8080/instantwin/bank/api";

    private final RestClient restClient;

    public SlotRequestTransactionClient(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl(BASE_URL)
                .build();
    }

    @Override
    public ResponseEntity<String> requestTransaction(long userId, BigDecimal amount) {
        var responseAsString = restClient.post()
                .uri("/transaction/user/{userId}", userId)
                .body(new SlotRequestTransactionDTO(
                        invoicingParty, amount))
                .retrieve()
                .toEntity(String.class);

        return responseAsString;
    }
}
