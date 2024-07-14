package com.somecompany.transferservice.dto;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class CurrencyConversionResultDto {
    private BigDecimal deductFromOriginAcc;
    private BigDecimal creditToRecipientAcc;
}
