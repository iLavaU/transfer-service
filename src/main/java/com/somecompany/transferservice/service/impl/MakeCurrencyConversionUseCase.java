package com.somecompany.transferservice.service.impl;

import com.somecompany.transferservice.client.OpenExchangeRatesAPIClient;
import com.somecompany.transferservice.dto.CurrencyConversionRequestDTO;
import com.somecompany.transferservice.dto.OpenExchangeRatesLatestDTO;
import com.somecompany.transferservice.exception.CurrencyNotListedInAPIException;
import com.somecompany.transferservice.service.UseCase;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class MakeCurrencyConversionUseCase implements UseCase<CurrencyConversionRequestDTO, BigDecimal> {

    private final OpenExchangeRatesAPIClient oerClient;

    public MakeCurrencyConversionUseCase(OpenExchangeRatesAPIClient oerClient) {
        this.oerClient = oerClient;
    }

    @Override
    public BigDecimal execute(CurrencyConversionRequestDTO input) {
        BigDecimal amount = input.getAmount();
        OpenExchangeRatesLatestDTO oerDTO = oerClient.getLatestOpenExchangeRates();

        BigDecimal fromValue = oerDTO.getRates().computeIfPresent(input.getFromCurrency(), (k, v) -> v);
        if (fromValue == null) {
            throw new CurrencyNotListedInAPIException(input.getFromCurrency());
        }

        BigDecimal toValue = oerDTO.getRates().computeIfPresent(input.getToCurrency(), (k, v) -> v);
        if (toValue == null) {
            throw new CurrencyNotListedInAPIException(input.getToCurrency());
        }

        return fromValue.multiply(toValue).multiply(amount);
    }
}
