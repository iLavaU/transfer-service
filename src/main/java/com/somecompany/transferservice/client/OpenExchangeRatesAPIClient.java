package com.somecompany.transferservice.client;

import com.somecompany.transferservice.dto.OpenExchangeRatesLatestDto;
import com.somecompany.transferservice.exception.OerApiLatestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Objects;

@Slf4j
@Component
public class OpenExchangeRatesAPIClient {

    private final String apiKey;
    private final RestClient restClient;

    public OpenExchangeRatesAPIClient(@Qualifier("oerRestClient") RestClient restClient, @Value("${open.exchange.rates.api.appID}") String apiKey) {
        this.restClient = restClient;
        this.apiKey = apiKey;
    }

    public OpenExchangeRatesLatestDto getLatestOpenExchangeRates() {
        log.info("Getting latest exchange rates from Open Exchange Rates API.");
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/latest.json")
                        .queryParam("app_id", apiKey)
                        .build()
                )
                .accept(MediaType.APPLICATION_JSON)
                .exchange((request, response) -> {
                    if (response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError()){
                        throw new OerApiLatestException(response.getStatusCode());
                    }
                    log.info("Succesfully retrieved OER rates.");
                    return Objects.requireNonNull(response.bodyTo(OpenExchangeRatesLatestDto.class));
                });
    }
}
