package com.somecompany.transferservice.service;

import com.somecompany.transferservice.TestDataUtil;
import com.somecompany.transferservice.client.OpenExchangeRatesAPIClient;
import com.somecompany.transferservice.dto.CurrencyConversionResultDto;
import com.somecompany.transferservice.dto.transfer.CurrencyConversionRequestDto;
import com.somecompany.transferservice.service.impl.CurrencyConversionUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.MathContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CurrencyConversionUseCaseTest {

    @Mock
    OpenExchangeRatesAPIClient oerClient;
    @InjectMocks
    CurrencyConversionUseCase currencyConversionUseCase;
    TestDataUtil testDataUtil = new TestDataUtil();

    @Test
    void testCurrencyConversionInOriginCurrency() {
        when(oerClient.getLatestOpenExchangeRates()).thenReturn(testDataUtil.OER_RESULT_DTO);
        BigDecimal EURToUSD = testDataUtil.OER_RESULT_DTO.rates().get("EUR");
        BigDecimal GBPToUSD = testDataUtil.OER_RESULT_DTO.rates().get("GBP");

        CurrencyConversionRequestDto requestDto = CurrencyConversionRequestDto.builder()
                .fromCurrency("EUR")
                .toCurrency("GBP")
                .amount(BigDecimal.valueOf(10))
                .inOriginCurrency(true)
                .build();

        CurrencyConversionResultDto result = currencyConversionUseCase.execute(requestDto);

        assertEquals(BigDecimal.valueOf(10), result.deductFromOriginAcc());
        assertEquals(BigDecimal.valueOf(10).divide(EURToUSD, MathContext.DECIMAL64).multiply(GBPToUSD), result.creditToRecipientAcc());
    }

    @Test
    void testCurrencyConversionNotInOriginCurrency() {
        when(oerClient.getLatestOpenExchangeRates()).thenReturn(testDataUtil.OER_RESULT_DTO);
        BigDecimal EURToUSD = testDataUtil.OER_RESULT_DTO.rates().get("EUR");
        BigDecimal GBPToUSD = testDataUtil.OER_RESULT_DTO.rates().get("GBP");

        CurrencyConversionRequestDto requestDto = CurrencyConversionRequestDto.builder()
                .fromCurrency("EUR")
                .toCurrency("GBP")
                .amount(BigDecimal.valueOf(10))
                .inOriginCurrency(false)
                .build();

        CurrencyConversionResultDto result = currencyConversionUseCase.execute(requestDto);

        assertEquals(BigDecimal.valueOf(10), result.creditToRecipientAcc());
        assertEquals(BigDecimal.valueOf(10).divide(GBPToUSD, MathContext.DECIMAL64).multiply(EURToUSD), result.deductFromOriginAcc());
    }
}
