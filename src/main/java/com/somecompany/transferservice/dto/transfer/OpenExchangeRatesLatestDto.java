package com.somecompany.transferservice.dto.transfer;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Map;

public record OpenExchangeRatesLatestDto (
    String disclaimer,
    String license,
    String timestamp,
    String base,
    Map<String, BigDecimal> rates
) {}
