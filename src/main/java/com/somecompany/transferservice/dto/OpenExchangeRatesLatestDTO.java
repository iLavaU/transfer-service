package com.somecompany.transferservice.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Map;

@Builder
@Getter
public class OpenExchangeRatesLatestDTO {
    private String disclaimer;
    private String license;
    private String timestamp;
    private String base;
    private Map<String, BigDecimal> rates;
}
