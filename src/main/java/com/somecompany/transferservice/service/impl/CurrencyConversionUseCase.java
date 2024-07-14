package com.somecompany.transferservice.service.impl;

import com.somecompany.transferservice.client.OpenExchangeRatesAPIClient;
import com.somecompany.transferservice.dto.CurrencyConversionResultDto;
import com.somecompany.transferservice.dto.transfer.CurrencyConversionRequestDto;
import com.somecompany.transferservice.dto.transfer.OpenExchangeRatesLatestDto;
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

        BigDecimal fromValue = oerDTO.rates().computeIfPresent(input.getFromCurrency(), (k, v) -> v);
        if (fromValue == null) {
            throw new CurrencyNotListedInAPIException(input.getFromCurrency());
        }

        BigDecimal toValue = oerDTO.rates().computeIfPresent(input.getToCurrency(), (k, v) -> v);
        if (toValue == null) {
            throw new CurrencyNotListedInAPIException(input.getToCurrency());
        }
        CurrencyConversionResultDto resultDto = new CurrencyConversionResultDto();
        if (input.getInOriginCurrency()) {
            resultDto.setDeductFromOriginAcc(input.getAmount());
            resultDto.setCreditToRecipientAcc(amount.divide(fromValue, mathContext).multiply(toValue));
        } else {
            resultDto.setDeductFromOriginAcc(amount.divide(toValue, mathContext).multiply(fromValue));
            resultDto.setCreditToRecipientAcc(input.getAmount());
        }
        return resultDto;
    }
}
