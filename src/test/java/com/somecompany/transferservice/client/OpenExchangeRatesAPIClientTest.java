package com.somecompany.transferservice.client;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OpenExchangeRatesAPIClientTest {

    @Mock
    RestClient restClient;
    @InjectMocks
    OpenExchangeRatesAPIClient openExchangeRatesAPIClient;

    @Test
    void getOpenExchangeRates() {
//        when(restClient.get()).thenReturn(); TODO: complete.
    }
}
