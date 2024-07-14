package com.somecompany.transferservice.dto.transfer;

import java.math.BigDecimal;

public record MakeTransferResultDto (
    String message,
    BigDecimal originAccountBalance,
    BigDecimal recipientAccountBalance
) {}
