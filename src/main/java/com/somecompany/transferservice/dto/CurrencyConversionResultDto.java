package com.somecompany.transferservice.dto;

import java.math.BigDecimal;

public record CurrencyConversionResultDto (
    BigDecimal deductFromOriginAcc,
    BigDecimal creditToRecipientAcc
) {}
