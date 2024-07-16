package com.somecompany.transferservice.service.impl;

import com.somecompany.transferservice.client.OpenExchangeRatesAPIClient;
import com.somecompany.transferservice.dto.CurrencyConversionResultDto;
import com.somecompany.transferservice.dto.request.CurrencyConversionRequestDto;
import com.somecompany.transferservice.dto.OpenExchangeRatesLatestDto;
import com.somecompany.transferservice.exception.CurrencyNotListedInAPIException;
import com.somecompany.transferservice.service.UseCase;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;

@Service
public class CurrencyConversionUseCase implements UseCase<CurrencyConversionRequestDto, CurrencyConversionResultDto> {

    private final OpenExchangeRatesAPIClient oerClient;
    private final MathContext mathContext = MathContext.DECIMAL64;

    public CurrencyConversionUseCase(OpenExchangeRatesAPIClient oerClient) {
        this.oerClient = oerClient;
    }

    @Override
    public CurrencyConversionResultDto execute(CurrencyConversionRequestDto input) {
        BigDecimal amount = input.getAmount();
        OpenExchangeRatesLatestDto oerDTO = oerClient.getLatestOpenExchangeRates();

        BigDecimal fromRate = getRate(oerDTO, input.getFromCurrency());
        BigDecimal toRate = getRate(oerDTO, input.getToCurrency());

        BigDecimal deductFromOriginAcc;
        BigDecimal creditToDestinyAcc;
        if (input.getInOriginCurrency()) {
            deductFromOriginAcc = input.getAmount();
            creditToDestinyAcc = amount.divide(fromRate, mathContext).multiply(toRate);
        } else {
            deductFromOriginAcc = amount.divide(toRate, mathContext).multiply(fromRate);
            creditToDestinyAcc = input.getAmount();
        }
        return new CurrencyConversionResultDto(deductFromOriginAcc, creditToDestinyAcc);
    }

    private static BigDecimal getRate(OpenExchangeRatesLatestDto oerDTO, String currency) {
        BigDecimal rate = oerDTO.rates().computeIfPresent(currency, (k, v) -> v);
        if (rate == null) {
            throw new CurrencyNotListedInAPIException(currency);
        }
        return rate;
    }
}
