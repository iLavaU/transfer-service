package com.somecompany.transferservice.client;

import com.somecompany.transferservice.dto.OpenExchangeRatesLatestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
public class OpenExchangeRatesAPIClient {

    private final String apiKey;
    private final RestClient restClient;

    public OpenExchangeRatesAPIClient(@Qualifier("oerRestClient") RestClient restClient, @Value("${open.exchange.rates.api.appID}") String apiKey) {
        this.restClient = restClient;
        this.apiKey = apiKey;
    }

    public OpenExchangeRatesLatestDTO getLatestOpenExchangeRates() {

        ResponseEntity<OpenExchangeRatesLatestDTO> entity = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("latest")
                        .queryParam("app_id", apiKey)
                        .build()
                )
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {

                })
                .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {

                })
                .toEntity(OpenExchangeRatesLatestDTO.class);


        return null;
    }
}
