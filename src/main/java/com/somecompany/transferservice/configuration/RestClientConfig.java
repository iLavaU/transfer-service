package com.somecompany.transferservice.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Value("${open.exchange.rates.api.url}")
    private String openExchangeRatesApiUrl;

    @Bean
    public RestClient oerRestClient() {
        return RestClient.builder()
                .baseUrl(openExchangeRatesApiUrl)
                .build();
    }
}
