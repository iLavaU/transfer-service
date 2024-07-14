package com.somecompany.transferservice.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
public class CurrencyConversionRequestDTO {
    private BigDecimal amount;
    private String fromCurrency;
    private String toCurrency;
}
