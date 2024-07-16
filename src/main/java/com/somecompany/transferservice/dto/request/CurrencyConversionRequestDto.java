package com.somecompany.transferservice.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
public class CurrencyConversionRequestDto {
    private BigDecimal amount;
    private String fromCurrency;
    private String toCurrency;
    private Boolean inOriginCurrency;
}
