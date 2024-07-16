package com.somecompany.transferservice.dto.response;

import java.math.BigDecimal;

public record MakeTransferResultDto (
    BigDecimal originAccountBalance,
    BigDecimal recipientAccountBalance
) {}
