package com.instantwin.roulette.client;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.instantwin.roulette.client.dto.BankTransactionRequest;
import com.instantwin.roulette.client.dto.BankTransactionResponse;
import com.instantwin.roulette.contract.client.IBankClient;

/**
 * DIP: Implementiert IBankClient – GameHandler hängt nur vom Interface ab.
 * SRP: Verantwortlich ausschließlich für die HTTP-Kommunikation mit dem Bank-Service.
 */
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

    /**
     * Prüft ob ein User im Bank-Service existiert.
     * Gibt false zurück wenn der User nicht gefunden wird oder ein Netzwerkfehler auftritt.
     */
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

    /**
     * Überweist den Gewinnbetrag auf das Konto des Users via Bank-API.
     * invoicingParty "ROULETTE" wird vom Bank-Service als TransactionInvoicingParty.ROULETTE deserialisiert.
     */
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
