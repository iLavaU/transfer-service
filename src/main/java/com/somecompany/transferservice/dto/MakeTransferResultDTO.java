package com.somecompany.transferservice.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public class MakeTransferResultDTO {
    private BigDecimal originAccountBalance;
    private BigDecimal recipientAccountBalance;
}
